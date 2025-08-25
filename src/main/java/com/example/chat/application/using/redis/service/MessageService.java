package com.example.chat.application.using.redis.service;


import com.example.chat.application.using.redis.model.Message;
import com.example.chat.application.using.redis.redis.MessagePublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


@Service
public class MessageService {
    private final RedisTemplate<String, String> redis;
    private final ObjectMapper objectMapper;
    private final MessagePublisher publisher;


    public MessageService(RedisTemplate<String, String> redis, ObjectMapper objectMapper, MessagePublisher publisher) {
        this.redis = redis;
        this.objectMapper = objectMapper;
        this.publisher = publisher;
    }

    public void sendMessage(String roomId, Message msg) {
        String key = messagesKey(roomId);
        String json = toJson(msg);
        redis.opsForList().rightPush(key, json);
        publisher.publish(roomId, json);
    }


    /**
     *
      * @param roomId
     * @param limit
     * @return
     */
    public List<Message> getLastMessages(String roomId, int limit) {
        String key = messagesKey(roomId);
        Long size = redis.opsForList().size(key);
        if (size == null || size == 0) return List.of();
        long start = Math.max(0, size - limit);
        List<String> raw = redis.opsForList().range(key, start, size - 1);
        List<Message> out = new ArrayList<>();
        if (raw != null) {
            for (String r : raw) {
                try {
                    out.add(objectMapper.readValue(r, Message.class));
                } catch (Exception ignored) {}
            }
        }
        return out;
    }



    private String messagesKey(String roomId) { return "chat:room:" + roomId + ":messages"; }


    private String toJson(Message msg) {
        try {
            return objectMapper.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }
}