package com.h.udemy.java.uservices.restaurant.domain.service;

import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.restaurant.domain.core.RestaurantDomainService;
import com.h.udemy.java.uservices.restaurant.domain.core.entity.Restaurant;
import com.h.udemy.java.uservices.restaurant.domain.core.event.OrderApprovalEvent;
import com.h.udemy.java.uservices.restaurant.domain.core.exception.RestaurantNotFoundException;
import com.h.udemy.java.uservices.restaurant.domain.service.dto.RestaurantApprovalRequest;
import com.h.udemy.java.uservices.restaurant.domain.service.mapper.RestaurantDataMapper;
import com.h.udemy.java.uservices.restaurant.domain.service.ports.output.message.publisher.IOrderApprovedMessagePublisher;
import com.h.udemy.java.uservices.restaurant.domain.service.ports.output.message.publisher.IOrderRejectedMessagePublisher;
import com.h.udemy.java.uservices.restaurant.domain.service.ports.output.repository.IOrderApprovalRepository;
import com.h.udemy.java.uservices.restaurant.domain.service.ports.output.repository.IRestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_RESTAURANT_NOT_FOUND;

@Slf4j
@Service
public class RestaurantApprovalRequestHelper {
    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantDataMapper restaurantMapper;
    private final IRestaurantRepository restaurantRepository;
    private final IOrderApprovalRepository orderApprovalRepository;
    private final IOrderApprovedMessagePublisher approvedMessagePublisher;
    private final IOrderRejectedMessagePublisher rejectedMessagePublisher;


    public RestaurantApprovalRequestHelper(RestaurantDomainService restaurantDomainService,
                                           RestaurantDataMapper restaurantMapper,
                                           IRestaurantRepository restaurantRepository,
                                           IOrderApprovalRepository orderApprovalRepository,
                                           IOrderApprovedMessagePublisher approvedMessagePublisher,
                                           IOrderRejectedMessagePublisher rejectedMessagePublisher) {

        this.restaurantDomainService = restaurantDomainService;
        this.restaurantMapper = restaurantMapper;
        this.restaurantRepository = restaurantRepository;
        this.orderApprovalRepository = orderApprovalRepository;
        this.approvedMessagePublisher = approvedMessagePublisher;
        this.rejectedMessagePublisher = rejectedMessagePublisher;
    }


    @Transactional
    public OrderApprovalEvent persistOrderApproval(RestaurantApprovalRequest approvalRequest) {

        log.info("Processing restaurant approval for order id: {0}", approvalRequest.getOrderId());

        List<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(approvalRequest);

        OrderApprovalEvent orderApprovalEvent = restaurantDomainService.validateOrder(
                        restaurant,
                        failureMessages,
                        approvedMessagePublisher,
                        rejectedMessagePublisher);

        orderApprovalRepository.save(restaurant.getOrderApproval());
        return orderApprovalEvent;
    }

    private Restaurant findRestaurant(RestaurantApprovalRequest approvalRequest) {

        Restaurant restaurant = restaurantMapper
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

}
