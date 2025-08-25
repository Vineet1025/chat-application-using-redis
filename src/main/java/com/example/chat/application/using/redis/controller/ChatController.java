package com.example.chat.application.using.redis.controller;

import com.example.chat.application.using.redis.dto.CreateRoomRequest;
import com.example.chat.application.using.redis.dto.JoinRoomRequest;
import com.example.chat.application.using.redis.dto.SendMessageRequest;
import com.example.chat.application.using.redis.model.Message;
import com.example.chat.application.using.redis.service.ChatRoomService;
import com.example.chat.application.using.redis.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/chatapp")
public class ChatController {


    private final ChatRoomService chatRoomService;
    private final MessageService messageService;


    public ChatController(ChatRoomService chatRoomService, MessageService messageService) {
        this.chatRoomService = chatRoomService;
        this.messageService = messageService;
    }

    @PostMapping("/chatrooms")
    public ResponseEntity<?> createRoom(@Valid @RequestBody CreateRoomRequest req) {
        boolean created = chatRoomService.createRoom(req.getRoomName());
        if (!created) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Chat room '" + req.getRoomName() + "' already exists"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Chat room '" + req.getRoomName() + "' created successfully.",
                "roomId", req.getRoomName()
        ));
    }

    @GetMapping("/chatrooms")
    public ResponseEntity<Set<String>> listRooms() {
        return ResponseEntity.ok(chatRoomService.getAllRooms());
    }


    @PostMapping("/chatrooms/{roomId}/join")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId, @Valid @RequestBody JoinRoomRequest req) {
        if (!chatRoomService.roomExists(roomId)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Chat room '" + roomId + "' does not exist"
            ));
        }
        chatRoomService.addParticipant(roomId, req.getParticipant());
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "User '" + req.getParticipant() + "' joined chat room '" + roomId + "'."
        ));
    }

    @PostMapping("/chatrooms/{roomId}/messages")
    public ResponseEntity<?> sendMessage(@PathVariable String roomId, @Valid @RequestBody SendMessageRequest req) {
        if (!chatRoomService.roomExists(roomId)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Chat room '" + roomId + "' does not exist"
            ));
        }
        chatRoomService.addParticipant(roomId, req.getParticipant());


        Message msg = new Message(req.getParticipant(), req.getMessage());
        messageService.sendMessage(roomId, msg);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Message sent successfully.",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    @GetMapping("/chatrooms/{roomId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable String roomId, @RequestParam(defaultValue = "10") int limit) {
        if (!chatRoomService.roomExists(roomId)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Chat room '" + roomId + "' does not exist"
            ));
        }
        List<Message> messages = messageService.getLastMessages(roomId, Math.max(1, limit));
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "messages", messages
        ));
    }
}



