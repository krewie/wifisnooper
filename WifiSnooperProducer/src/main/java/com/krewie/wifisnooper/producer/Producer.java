package com.krewie.wifisnooper.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.kafka.support.SendResult;

public class Producer {

    public static final Logger log = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate<String, Device> kafkaTemplate;

    @Value("${kafka.topic}")
    private String topic;

    public void send(Device message) {
        send(topic, message);
    }

    private void send(String topic, Device message) {
        ListenableFuture<SendResult<String, Device>> future = kafkaTemplate.send(topic, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Device>>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("message: "+message);
                log.error("unable to send message='{}'", message, ex);
            }

            @Override
            public void onSuccess(SendResult<String, Device> result) {
                System.out.println("message: "+message);
                log.info("sent message='{}' with offset='{}'");
            }
        });
    }
}
