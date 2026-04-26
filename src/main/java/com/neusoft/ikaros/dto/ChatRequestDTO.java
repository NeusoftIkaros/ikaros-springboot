package com.neusoft.ikaros.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDTO {

    private Long userId;
    private String question;
    private String mode;
    private String sessionId;
}