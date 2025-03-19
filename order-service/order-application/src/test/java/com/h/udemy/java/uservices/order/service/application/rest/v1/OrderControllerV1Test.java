package com.h.udemy.java.uservices.order.service.application.rest.v1;

import com.h.udemy.java.uservices.order.service.application.test.util.config.EnvConfigTestCase;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.entity.Customer;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.CustomerRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.OrderRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.PaymentOutboxRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.h.udemy.java.uservices.order.service.application.test.util.factory.CustomerFactory.createCustomer;
import static com.h.udemy.java.uservices.order.service.application.test.util.factory.OrderFactory.*;
import static com.h.udemy.java.uservices.order.service.application.test.util.factory.RestaurantFactory.createRestaurant;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
class OrderControllerV1Test extends EnvConfigTestCase {

    private final String baseUrl = "/v1/orders";

    private final Customer customer = createCustomer();
    private final Order orderSaved = createOrderSaved();
    private final Restaurant restaurant = createRestaurant();
    private final CreateOrderCommand createOrderCommand = createCreateOrderCommand();
    private final OrderPaymentOutboxMessage orderPaymentOutboxMessage = createOrderPaymentOutboxMessage();

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    PaymentOutboxRepository paymentOutboxRepository;

    @BeforeAll
    public void setup() {

        when(this.customerRepository.findCustomer(customer.getId().getValue()))
                .thenReturn(Optional.of(customer));

        when(this.restaurantRepository.findRestaurantInformation(restaurant))
                .thenReturn(Optional.of(restaurant));
    }

    @Test
    void when_createOrder_should_return_201() throws Exception {
        when(this.orderRepository.insertOrder(any(Order.class)))
                .thenReturn(orderSaved);
        when(paymentOutboxRepository.save(any(OrderPaymentOutboxMessage.class)))
                .thenReturn(orderPaymentOutboxMessage);

        mockMvc.perform(post(baseUrl)
                        .header(HttpHeaders.ACCEPT, "application/vnd.api.v1+json")
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
                .thenReturn(Optional.of(orderSaved));

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
                .contains(HttpStatus.NOT_FOUND.getReasonPhrase()));
    }

    @Test
    void when_fetchAllOrder_should_return_200() throws Exception {
        when(this.orderRepository.fetchAll())
                .thenReturn(List.of(orderSaved));

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