package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.domain.messages.Msgs;
import com.h.udemy.java.uservices.domain.valueobject.ProductId;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.Product;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCancelledEvent;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;
import com.h.udemy.java.uservices.order.service.domain.event.publisher.DomainEventPublisher;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.h.udemy.java.uservices.domain.Const.ZONED_UTC;

@Slf4j
@Service
public class OrderDomainService implements IOrderDomainService {

    private static final ZoneId ZONE = ZoneId.of("UTC");
    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info(Msgs.ORDER_ID_INITIATED.get(), order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZONED_UTC));
    }

    @Override
    public OrderPaidEvent payOrder(Order order,
                                   DomainEventPublisher<OrderPaidEvent> orderPaidEventDomainEventPublisher) {
        order.pay();
        log.info("Order with id: {} is paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZONED_UTC), orderPaidEventDomainEventPublisher);
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info(Msgs.ORDER_ID_PAYMENT_CANCELLING.get());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZONED_UTC));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info(Msgs.ORDER_ID_PAYMENT_CANCELLED.get());
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException(Msgs.ERR_RESTAURANT_ID_NOT_ACTIVE.get()
                    + restaurant.getId().getValue());
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {

        long timeStamp = System.nanoTime();

        //todo: remove this code after performonce diagnosys
        // n*n = O(n^2) -> exp
        order.getItems().forEach(orderItem -> restaurant.getProducts().forEach(product -> {
            Product currentProduct = orderItem.getProduct();
            if (currentProduct.equals(product)) {
                currentProduct.updateWithConfirmedNameAndPrice(product);
            }
        }));

        System.out.println(":: -> time diff: " + (System.nanoTime() - timeStamp));
        timeStamp = System.nanoTime();



        // n + n = O(2n) -> linear
        Map<ProductId, Product> restaurantProductMap = new HashMap<>();
        restaurant.getProducts().forEach(restaurantProduct ->{
            restaurantProductMap.put(restaurantProduct.getId(), restaurantProduct);
        });
        order.getItems().forEach(orderItem ->  {
            Product currentProduct = orderItem.getProduct();
            Product restaurantProduct = restaurantProductMap.get(currentProduct.getId());
            currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct);
        });

        System.out.println(":: -> time diff: " + (System.nanoTime() - timeStamp));
    }
}
