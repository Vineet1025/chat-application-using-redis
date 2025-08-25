package com.example.chat.application.using.redis.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;


@Component
public class MessagePublisher {
    private final RedisTemplate<String, String> redisTemplate;


    public MessagePublisher(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void publish(String roomId, String messageJson) {
        String channel = "room:" + roomId;
        redisTemplate.convertAndSend(channel, messageJson);
    }
}