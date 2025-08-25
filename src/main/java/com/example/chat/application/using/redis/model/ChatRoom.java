package com.example.chat.application.using.redis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    private String roomId;
    private String name;
    private Set<String> participants;



}
