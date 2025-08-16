package com.example.saga.common.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaCommonConfig {

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
                Map.of(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                        org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                        JsonDeserializer.TRUSTED_PACKAGES, "*"
                ),
                new StringDeserializer(),
                jsonDeserializer
        ));
        return factory;
    }

    // Topics
    @Bean public NewTopic t1() { return new NewTopic("order-created", 1, (short)1); }
    @Bean public NewTopic t2() { return new NewTopic("payment-completed", 1, (short)1); }
    @Bean public NewTopic t3() { return new NewTopic("payment-failed", 1, (short)1); }
    @Bean public NewTopic t4() { return new NewTopic("stock-reserved", 1, (short)1); }
    @Bean public NewTopic t5() { return new NewTopic("stock-reservation-failed", 1, (short)1); }
    @Bean public NewTopic t6() { return new NewTopic("order-shipped", 1, (short)1); }
    @Bean public NewTopic t7() { return new NewTopic("payment-refunded", 1, (short)1); }
    @Bean public NewTopic t8() { return new NewTopic("order-failed", 1, (short)1); }
    @Bean public NewTopic t9() { return new NewTopic("inventory-completed", 1, (short)1); }
    @Bean public NewTopic t10() { return new NewTopic("inventory-failed", 1, (short)1); }
}
