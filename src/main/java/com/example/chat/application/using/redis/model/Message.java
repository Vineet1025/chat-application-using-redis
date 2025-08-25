package com.example.chat.application.using.redis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String participant;
    private String message;
    private LocalDateTime timestamp;


    public Message(String participant, String message) {
        this.participant = participant;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }


}