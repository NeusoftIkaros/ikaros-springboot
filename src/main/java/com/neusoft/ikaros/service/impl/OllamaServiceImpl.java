package com.neusoft.ikaros.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.ikaros.entity.ChatSession;
import com.neusoft.ikaros.entity.QaRecord;
import com.neusoft.ikaros.mapper.ChatSessionMapper;
import com.neusoft.ikaros.mapper.QaRecordMapper;
import com.neusoft.ikaros.service.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class OllamaServiceImpl implements OllamaService {

    private static final int CONNECT_TIMEOUT_MS = 10_000;
    private static final int READ_TIMEOUT_MS = 180_000;

    @Autowired
    private QaRecordMapper qaRecordMapper;

    @Autowired
    private ChatSessionMapper chatSessionMapper;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String chat(String question, String mode, String sessionId, Long userId) {

        HttpURLConnection conn = null;

        try {

            String prefix = "detailed".equalsIgnoreCase(mode)
                    ? "[Mode: Detailed]\n"
                    : "[Mode: Concise]\n";

            List<Map<String, String>> messages = new ArrayList<>();

            ChatSession session = chatSessionMapper.selectOne(
                    new QueryWrapper<ChatSession>()
                            .eq("session_id", sessionId)
                            .eq("user_id", userId)
            );

            List<QaRecord> history = qaRecordMapper.selectBySessionId(userId, sessionId);

            if (history != null && !history.isEmpty()) {
                int start = Math.max(0, history.size() - 10);

                for (int i = start; i < history.size(); i++) {
                    QaRecord r = history.get(i);
                    messages.add(Map.of("role", "user", "content", r.getQuestion()));
                    messages.add(Map.of("role", "assistant", "content", r.getAnswer()));
                }
            }

            messages.add(Map.of("role", "user", "content", prefix + question));

            Map<String, Object> body = new HashMap<>();
            body.put("model", "neusoft-ikaros");
            body.put("messages", messages);
            body.put("stream", false);

            String json = mapper.writeValueAsString(body);

            conn = (HttpURLConnection)
                    new URL("http://localhost:11434/api/chat").openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(READ_TIMEOUT_MS);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            InputStream responseStream = status >= 400 ? conn.getErrorStream() : conn.getInputStream();

            if (responseStream == null) {
                throw new RuntimeException("Ollama returned empty response stream, status=" + status);
            }

            if (status >= 400) {
                try (BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(responseStream, StandardCharsets.UTF_8)
                )) {
                    StringBuilder errorBody = new StringBuilder();
                    String errorLine;

                    while ((errorLine = errorReader.readLine()) != null) {
                        errorBody.append(errorLine);
                    }

                    throw new RuntimeException("Ollama returned status " + status + ": " + errorBody);
                }
            }

            StringBuilder fullAnswer = new StringBuilder();
            String line;

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(responseStream, StandardCharsets.UTF_8)
            )) {
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty()) continue;

                    JsonNode node = mapper.readTree(line);

                    JsonNode content = node.path("message").path("content");
                    if (!content.isMissingNode()) {
                        fullAnswer.append(content.asText());
                    }
                }
            }

            return fullAnswer.toString();

        } catch (Exception e) {
            throw new RuntimeException("Ollama call failed", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
