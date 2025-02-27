package com.h.udemy.java.uservices.restaurant.domain.service;

import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.restaurant.domain.core.RestaurantDomainServiceI;
import com.h.udemy.java.uservices.restaurant.domain.core.entity.Restaurant;
import com.h.udemy.java.uservices.restaurant.domain.core.event.OrderApprovalEvent;
import com.h.udemy.java.uservices.restaurant.domain.core.exception.RestaurantNotFoundException;
import com.h.udemy.java.uservices.restaurant.domain.service.dto.RestaurantApprovalRequest;
import com.h.udemy.java.uservices.restaurant.domain.service.mapper.RestaurantDataMapper;
import com.h.udemy.java.uservices.restaurant.domain.service.outbox.model.OrderOutboxMessage;
import com.h.udemy.java.uservices.restaurant.domain.service.outbox.scheduler.OrderOutboxHelper;
import com.h.udemy.java.uservices.restaurant.domain.service.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher;
import com.h.udemy.java.uservices.restaurant.domain.service.ports.output.repository.OrderApprovalRepository;
import com.h.udemy.java.uservices.restaurant.domain.service.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_RESTAURANT_NOT_FOUND;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_MESSAGE_ALREADY_SAVED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.PUB_PROCESSING_APPROVAL_FOR_ID;
import static java.text.MessageFormat.format;

@Slf4j
@Service
public class RestaurantApprovalRequestHelper {
    private final RestaurantDomainServiceI restaurantDomainService;
    private final RestaurantDataMapper restaurantDataMapper;
    private final RestaurantRepository restaurantRepository;
    private final OrderApprovalRepository orderApprovalRepository;
    private final OrderOutboxHelper orderOutboxHelper;
    private final RestaurantApprovalResponseMessagePublisher restaurantApprovalResponseMessagePublisher;


    public RestaurantApprovalRequestHelper(RestaurantDomainServiceI restaurantDomainService,
                                           RestaurantDataMapper restaurantMapper,
                                           RestaurantRepository restaurantRepository,
                                           OrderApprovalRepository orderApprovalRepository,
                                           OrderOutboxHelper orderOutboxHelper,
                                           RestaurantApprovalResponseMessagePublisher restaurantApprovalResponseMessagePublisher) {

        this.restaurantDomainService = restaurantDomainService;
        this.restaurantDataMapper = restaurantMapper;
        this.restaurantRepository = restaurantRepository;
        this.orderApprovalRepository = orderApprovalRepository;
        this.orderOutboxHelper = orderOutboxHelper;
        this.restaurantApprovalResponseMessagePublisher = restaurantApprovalResponseMessagePublisher;
    }


    @Transactional
    public void persistOrderApproval(RestaurantApprovalRequest restaurantApprovalRequest) {

        if (this.publishIfOutboxMessageProcessed(restaurantApprovalRequest)) {
            log.info(format(OUTBOX_MESSAGE_ALREADY_SAVED.build(restaurantApprovalRequest.getOrderId())));
            return;
        }

        log.info(PUB_PROCESSING_APPROVAL_FOR_ID.build(
                restaurantApprovalRequest.getOrderId()));

        List<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(restaurantApprovalRequest);

        OrderApprovalEvent orderApprovalEvent =
                restaurantDomainService.validateOrder(
                        restaurant,
                        failureMessages);

        orderApprovalRepository.save(restaurant.getOrderApproval());

        orderOutboxHelper.saveOrderOutboxMessage(
                restaurantDataMapper.orderApprovalEventToOrderEventPayload(orderApprovalEvent),
                orderApprovalEvent.getOrderApproval().getApprovalStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(restaurantApprovalRequest.getSagaId()));
    }

    private Restaurant findRestaurant(RestaurantApprovalRequest approvalRequest) {

        Restaurant restaurant = restaurantDataMapper
                .restaurantApprovalRequestToRestaurant(approvalRequest);

        Optional<Restaurant> restaurantDbOp = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantDbOp.isEmpty()) {
            String message = ERR_RESTAURANT_NOT_FOUND.build(restaurant.getId().getValue());
            log.error(message);
            throw new RestaurantNotFoundException(message);
        }

        Restaurant restaurantEntity = restaurantDbOp.get();

        restaurant.setActive(true);
        restaurant.getOrderDetail().getProducts().forEach(product ->
                restaurantEntity.getOrderDetail().getProducts().forEach(p -> {
                    if (p.getId().equals(product.getId())) {
                        product.updateWithConfirmedNamePriceAndAvailability(
                                p.getName(),
                                p.getPrice(),
                                p.isAvailable());
                    }
                }));
        restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(approvalRequest.getOrderId())));

        return restaurant;
    }

    private boolean publishIfOutboxMessageProcessed(RestaurantApprovalRequest restaurantApprovalRequest) {
        Optional<OrderOutboxMessage> orderOutboxMessage =
                orderOutboxHelper.getCompletedOrderOutboxMessageBySagaIdAndOutboxStatus(UUID
                        .fromString(restaurantApprovalRequest.getSagaId()), OutboxStatus.COMPLETED);
        if (orderOutboxMessage.isPresent()) {
            restaurantApprovalResponseMessagePublisher.publish(orderOutboxMessage.get(),
                    orderOutboxHelper::updateOutboxStatus);
            return true;
        }
        return false;
    }

}
