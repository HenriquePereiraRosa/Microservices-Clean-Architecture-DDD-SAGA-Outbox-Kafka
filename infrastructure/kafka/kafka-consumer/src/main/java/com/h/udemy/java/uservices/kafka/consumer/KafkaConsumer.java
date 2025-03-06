package com.h.udemy.java.uservices.kafka.consumer;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

public interface KafkaConsumer<T extends SpecificRecordBase> {

    void receive(List<T> message,
                 List<String> keys,
                 List<Integer> partitions,
                 List<Long> offset);
}
