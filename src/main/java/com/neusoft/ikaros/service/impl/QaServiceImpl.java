package com.neusoft.ikaros.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.ikaros.dto.ChatRequestDTO;
import com.neusoft.ikaros.entity.ChatSession;
import com.neusoft.ikaros.entity.QaRecord;
import com.neusoft.ikaros.mapper.ChatSessionMapper;
import com.neusoft.ikaros.mapper.QaRecordMapper;
import com.neusoft.ikaros.service.OllamaService;
import com.neusoft.ikaros.service.QaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QaServiceImpl implements QaService {

    @Autowired
    private OllamaService ollamaService;

    @Autowired
    private QaRecordMapper qaRecordMapper;

    @Autowired
    private ChatSessionMapper chatSessionMapper;

    @Override
    public String chat(ChatRequestDTO dto) {

        String sessionId = dto.getSessionId();

        ChatSession session = chatSessionMapper.selectOne(
                new QueryWrapper<ChatSession>()
                        .eq("session_id", sessionId)
                        .eq("user_id", dto.getUserId())
        );

        if (session == null) {
            session = new ChatSession();
            session.setSessionId(sessionId);
            session.setUserId(dto.getUserId());
            session.setTitle(dto.getQuestion());
            session.setLastTime(LocalDateTime.now());
            session.setCreatedAt(LocalDateTime.now());

            chatSessionMapper.insert(session);
        } else {
            ChatSession update = new ChatSession();
            update.setId(session.getId());
            update.setLastTime(LocalDateTime.now());

            chatSessionMapper.updateById(update);
        }

        String answer = ollamaService.chat(
                dto.getQuestion(),
                dto.getMode(),
                sessionId,
                dto.getUserId()
        );

        QaRecord record = new QaRecord();
        record.setUserId(dto.getUserId());
        record.setSessionId(sessionId);
        record.setQuestion(dto.getQuestion());
        record.setAnswer(answer);
        record.setMode(dto.getMode());
        record.setCreateTime(LocalDateTime.now());

        qaRecordMapper.insert(record);

        return answer;
    }

    @Override
    public List<QaRecord> search(Long userId, String keyword) {
        return qaRecordMapper.searchByKeyword(userId, keyword);
    }
}
