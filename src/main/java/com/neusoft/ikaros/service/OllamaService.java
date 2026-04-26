package com.neusoft.ikaros.service;

import com.neusoft.ikaros.dto.ChatRequestDTO;

public interface OllamaService {
    String chat(String question, String mode, String sessionId, Long userId);
}