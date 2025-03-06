package com.h.udemy.java.uservices.order.service.domain;

import static com.h.udemy.java.uservices.domain.Constants.ZONED_UTC;
import static com.h.udemy.java.uservices.domain.Constants.getZonedDateTimeNow;
import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_RESTAURANT_ID_NOT_ACTIVE;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.h.udemy.java.uservices.domain.messages.Messages;
import com.h.udemy.java.uservices.domain.valueobject.ProductId;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.Product;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCancelledEvent;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order,
                                                      Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();

        log.info(Messages.ORDER_ID_INITIATED.build(order.getId().getValue()));

        return new OrderCreatedEvent(order,
                ZonedDateTime.now(ZONED_UTC));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id: {} is paid", order.getId().getValue()); // todo: change this messages
        return new OrderPaidEvent(order, getZonedDateTimeNow());
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order,
                                                  List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info(Messages.ORDER_ID_PAYMENT_CANCELLING.build());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZONED_UTC));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info(Messages.ORDER_ID_PAYMENT_CANCELLED.build());
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException(ERR_RESTAURANT_ID_NOT_ACTIVE
                    .get() + restaurant.getId().getValue());
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {

        long timeStamp = System.nanoTime();

        //todo: remove this code after performance diagnoses
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
        restaurant.getProducts().forEach(restaurantProduct ->
                restaurantProductMap.put(restaurantProduct.getId(), restaurantProduct));
        order.getItems().forEach(orderItem ->  {
            Product currentProduct = orderItem.getProduct();
            Product restaurantProduct = restaurantProductMap.get(currentProduct.getId());
            currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct);
        });

        System.out.println(":: -> time diff: " + (System.nanoTime() - timeStamp));
    }
}
