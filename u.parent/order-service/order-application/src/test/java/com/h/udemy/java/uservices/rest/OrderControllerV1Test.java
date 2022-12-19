package com.h.udemy.java.uservices.rest;

import com.h.udemy.java.uservices.ApiEnvTest;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.entity.Customer;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.ICustomerRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IOrderRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IRestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.h.udemy.java.uservices.factory.CustomerFactory.createCustomer;
import static com.h.udemy.java.uservices.factory.OrderFactory.createCreateOrderCommand;
import static com.h.udemy.java.uservices.factory.OrderFactory.createOrderCreatedEvent;
import static com.h.udemy.java.uservices.factory.OrderFactory.createOrderSaved;
import static com.h.udemy.java.uservices.factory.RestaurantFactory.createRestaurant;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class OrderControllerV1Test extends ApiEnvTest {

    private final String baseUrl = "/v1/orders";

    private Customer customer = createCustomer();
    private Order orderSaved = createOrderSaved();
    private Restaurant restaurant = createRestaurant();
    private CreateOrderCommand createOrderCommand = createCreateOrderCommand();
    private OrderCreatedEvent orderCreatedEvent = createOrderCreatedEvent(orderSaved);

    @Autowired
    IOrderRepository orderRepository;
    @Autowired
    ICustomerRepository customerRepository;
    @Autowired
    IRestaurantRepository restaurantRepository;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(this.customerRepository.findCustomer(customer.getId().getValue()))
                .thenReturn(Optional.of(customer));

        when(this.restaurantRepository.findRestaurantInformation(restaurant))
                .thenReturn(Optional.ofNullable(restaurant));
    }


    @Test
    void when_createOrder_should_return_201() throws Exception {
        when(this.orderRepository.insertOrder(any(Order.class)))
                .thenReturn(orderSaved);

        mockMvc.perform(post(baseUrl)
                        .headers(new HttpHeaders())
                        .content(mapper.writeValueAsString(createOrderCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.trackingId").isNotEmpty())
                .andExpect(jsonPath("$.message")
                        .value("Order created successfully"))
                .andReturn();
    }

    @Test
    void when_fetchOrderByTrackingId_should_return_200() throws Exception {
        when(this.orderRepository.findByTrackingId(orderSaved.getTrackingId()))
                .thenReturn(Optional.ofNullable(orderSaved));

        MvcResult res = mockMvc.perform(get(baseUrl + "/by-tracking-id?trackingId=" +
                        orderSaved.getTrackingId().getValue())
                        .headers(new HttpHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderTrackingId").isNotEmpty())
                // todo: solve UUID not equal problem
//                .andExpect(jsonPath("$.orderTrackingId")
//                        .value(orderSaved.getTrackingId().getValue()))
                .andReturn();

        assertTrue(res.getResponse().getContentAsString()
                .contains(orderSaved.getTrackingId().getValue().toString()));
    }

    @Test
    void when_fetchOrderByTrackingId_with_wrong_UUID_should_return_404() throws Exception {
        UUID lSearchUUID = UUID.randomUUID();

        MvcResult res = mockMvc.perform(get(baseUrl + "/by-tracking-id?trackingId=" +
                        lSearchUUID)
                        .headers(new HttpHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andReturn();

        assertTrue(res.getResponse().getContentAsString()
                .contains(lSearchUUID.toString()));
    }

    @Test
    void when_fetchAllOrder_should_return_200() throws Exception {
        when(this.orderRepository.fetchAll())
                .thenReturn(Arrays.asList(orderSaved));

        MvcResult res = mockMvc.perform(get(baseUrl)
                        .headers(new HttpHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].orderTrackingId").isNotEmpty())
                .andReturn();

        assertTrue(res.getResponse().getContentAsString()
                .contains(orderSaved.getTrackingId().getValue().toString()));
    }
}