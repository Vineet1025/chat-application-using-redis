package com.example.chat.application.using.redis.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequest {
    @NotBlank
    private String participant;
    @NotBlank
    private String message;


}