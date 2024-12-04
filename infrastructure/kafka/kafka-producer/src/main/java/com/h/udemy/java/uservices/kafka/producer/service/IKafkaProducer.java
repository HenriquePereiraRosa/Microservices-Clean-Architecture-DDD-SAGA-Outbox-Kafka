package com.h.udemy.java.uservices.kafka.producer.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.Serializable;

public interface IKafkaProducer<K extends Serializable, V extends SpecificRecordBase> {
    void send(String topicName, K key, V message, ListenableFutureCallback<SendResult<K, V>> callback);
}
