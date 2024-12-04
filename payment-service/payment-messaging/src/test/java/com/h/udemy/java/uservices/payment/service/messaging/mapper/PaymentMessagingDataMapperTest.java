//package com.h.udemy.java.uservices.payment.service.messaging.mapper;
//
//import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
//import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentResponseAvroModel;
//import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCancelledEvent;
//import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;
//import com.h.udemy.java.uservices.payment.domain.core.event.PaymentFailedEvent;
//import com.h.udemy.java.uservices.payment.service.messaging.test.config.ApiEnvTest;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static com.h.udemy.java.uservices.payment.service.messaging.test.util.factory.PaymentEventFactory.createPaymentCompletedEvent;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class PaymentMessagingDataMapperTest extends ApiEnvTest {
//
//    @Autowired
//    private PaymentMessagingDataMapper mapper;
//
//    @Autowired
//    private IDomainEventPublisher<PaymentCompletedEvent> completedEventPublisher;
//    @Autowired
//    private IDomainEventPublisher<PaymentCancelledEvent> cancelledEventPublisher;
//    @Autowired
//    private IDomainEventPublisher<PaymentFailedEvent> failedEventPublisher;
//
//    @Test
//    void paymentCompletedEventToPaymentResponseAvroModel() {
//        PaymentCompletedEvent paymentCompletedEvent = createPaymentCompletedEvent(completedEventPublisher);
//        PaymentResponseAvroModel sut = mapper
//                .paymentCompletedEventToPaymentResponseAvroModel(paymentCompletedEvent);
//
//        assertEquals(paymentCompletedEvent.getPayment().getOrderId().getValue(), sut.getOrderId());
//
//    }
//
//    @Test
//    void paymentCancelledEventToPaymentResponseAvroModel() {
//    }
//
//    @Test
//    void paymentFailedEventToPaymentResponseAvroModel() {
//    }
//
//    @Test
//    void paymentRequestAvroModelToPaymentRequest() {
//    }

// todo: fix "payment-service.payment-request-topic-name" constructor parameter not found exception
