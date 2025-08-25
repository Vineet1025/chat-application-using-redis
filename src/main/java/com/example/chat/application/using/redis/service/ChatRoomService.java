package com.example.chat.application.using.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.Set;


@Service
public class ChatRoomService {
    private final RedisTemplate<String, String> redis;


    private static final String ROOMS_KEY = "chat:rooms";


    public ChatRoomService(RedisTemplate<String, String> redis) {
        this.redis = redis;
    }


    public boolean createRoom(String roomName) {
        Long added = redis.opsForSet().add(ROOMS_KEY, roomName);
        return added != null && added > 0;
    }


    public boolean roomExists(String roomId) {
        Boolean isMember = redis.opsForSet().isMember(ROOMS_KEY, roomId);
        return Boolean.TRUE.equals(isMember);
    }



    public Set<String> getAllRooms() {
        Set<String> members = redis.opsForSet().members(ROOMS_KEY);
        return members == null ? Set.of() : members;
    }


    public boolean addParticipant(String roomId, String participant) {
        if (!roomExists(roomId)) return false;
        Long added = redis.opsForSet().add(participantsKey(roomId), participant);
        return added != null && added > 0;
    }


    public Set<String> getParticipants(String roomId) {
        Set<String> members = redis.opsForSet().members(participantsKey(roomId));
        return members == null ? Set.of() : members;
    }


    private String participantsKey(String roomId) { return "chat:room:" + roomId + ":participants"; }
}