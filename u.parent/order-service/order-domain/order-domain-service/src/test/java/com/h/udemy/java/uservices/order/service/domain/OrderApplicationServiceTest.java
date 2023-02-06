package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.domain.messages.Messages;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.domain.valueobject.OrderStatus;
import com.h.udemy.java.uservices.domain.valueobject.ProductId;
import com.h.udemy.java.uservices.domain.valueobject.RestaurantId;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.dto.create.OrderAddressDTO;
import com.h.udemy.java.uservices.order.service.domain.dto.create.OrderItemDTO;
import com.h.udemy.java.uservices.order.service.domain.entity.Customer;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.Product;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainException;
import com.h.udemy.java.uservices.order.service.domain.mapper.OrderDataMapper;
import com.h.udemy.java.uservices.order.service.domain.ports.input.service.IOrderApplicationService;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.ICustomerRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IOrderRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IRestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderApplicationServiceTest extends ApiEnvTestConfig {

    @Autowired
    private IOrderApplicationService iOrderAplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private IOrderRepository iOrderRepository;

    @Autowired
    private ICustomerRepository iCustomerRepository;

    @Autowired
    private IRestaurantRepository iRestaurantRepository;


    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.randomUUID();
    private final UUID RESTAURANT_ID = UUID.randomUUID();
    private final UUID PRODUCT_ID = UUID.randomUUID();
    private final UUID ORDER_ID = UUID.randomUUID();
    private final BigDecimal PRICE = new BigDecimal("200.00");


    @BeforeAll
    public void setup() {
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddressDTO.builder()
                        .street("Sweet Street")
                        .postalCode("6900010")
                        .city("Gotham City")
                        .build())
                .price(PRICE)
                .items(List.of(OrderItemDTO.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .build(),
                        OrderItemDTO.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .build()))
                .build();

        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddressDTO.builder()
                        .street("Sweet Street")
                        .postalCode("6900010")
                        .city("Gotham City")
                        .build())
                .price(new BigDecimal("250.00")) // wrong price
                .items(List.of(OrderItemDTO.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .build(),
                        OrderItemDTO.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .build()))
                .build();

        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddressDTO.builder()
                        .street("Sweet Street")
                        .postalCode("6900010")
                        .city("Gotham City")
                        .build())
                .price(new BigDecimal("210.00")) // wrong price
                .items(List.of(OrderItemDTO.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("60.00")) // wrong product price
                                .build(),
                        OrderItemDTO.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .build()))
                .build();

        Customer customer = new Customer(new CustomerId(CUSTOMER_ID));

        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
                .products(List.of(new Product(new ProductId(PRODUCT_ID),
                                "product-1",
                                new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID),
                                "product-2",
                                new Money(new BigDecimal("50.00")))))
                .active(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        when(iCustomerRepository.findCustomer(CUSTOMER_ID))
                .thenReturn(Optional.of(customer));

        when(iRestaurantRepository.findRestaurantInformation(orderDataMapper
                .createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));

        when(iOrderRepository.insertOrder(any(Order.class)))
                .thenReturn(order);
    }


    @Test
    @org.junit.jupiter.api.Order(1)
    void createOrder_ok_success() {
        CreateOrderResponse createOrderResponse = iOrderAplicationService.createOrder(createOrderCommand);

        assertEquals(OrderStatus.PENDING, createOrderResponse.getOrderStatus());
        assertEquals(Messages.ORDER_CREATED_SUCCESSFULLY.get(), createOrderResponse.getMessage());
        assertNotNull(createOrderResponse.getTrackingId());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void createOrder_nok_WrongTotalPrice() {
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> {
                    iOrderAplicationService.createOrder(createOrderCommandWrongPrice);
                });

        final String exceptionMsg = Messages.ERR_ORDER_TOTAL_AND_ORDER_PRICES_DIFF.get()
                + ": (price) 250.00 != (orderItemsTotal) 200.00";

        assertEquals(exceptionMsg, orderDomainException.getMessage());
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void createOrder_nok_WrongProductPrice() {
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> {
                    iOrderAplicationService.createOrder(createOrderCommandWrongProductPrice);
                });

        final String exceptionMsg = Messages.ERR_ORDER_ITEM_PRICE_INVALID.get() + ": 210.00";

        assertEquals(exceptionMsg, orderDomainException.getMessage());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void createOrder_ok_WithPassiveRestaurant() {

        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
                .products(List.of(new Product(new ProductId(PRODUCT_ID),
                                "product-1",
                                new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID),
                                "product-2",
                                new Money(new BigDecimal("50.00")))

                ))
                .active(false) // inactive restaurant
                .build();

        when(iRestaurantRepository.findRestaurantInformation(orderDataMapper
                .createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));

        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> {
                    iOrderAplicationService.createOrder(createOrderCommand);
                });

        final String exceptionMsg = Messages.ERR_RESTAURANT_ID_NOT_ACTIVE.get() + RESTAURANT_ID;

        assertEquals(exceptionMsg, orderDomainException.getMessage());
    }
}