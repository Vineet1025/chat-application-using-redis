package com.example.chat.application.using.redis.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class MessageSubscriber {
    private static final Logger log = LoggerFactory.getLogger(MessageSubscriber.class);

    public void onMessage(String message, String channel) {
        log.info("[PUB/SUB] Channel: {} | Message: {}", channel, message);
    }
}