package com.neusoft.ikaros.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.ikaros.entity.ChatSession;
import com.neusoft.ikaros.entity.QaRecord;
import com.neusoft.ikaros.mapper.ChatSessionMapper;
import com.neusoft.ikaros.mapper.QaRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/session")
public class ChatSessionController {

    @Autowired
    private ChatSessionMapper chatSessionMapper;

    @Autowired
    private QaRecordMapper qaRecordMapper;

    @GetMapping("/list")
    public List<ChatSession> list(@RequestParam Long userId) {

        return chatSessionMapper.selectList(
                new QueryWrapper<ChatSession>()
                        .eq("user_id", userId)
                        .orderByDesc("last_time")
        );
    }

    @GetMapping("/search")
    public List<ChatSession> search(
            @RequestParam Long userId,
            @RequestParam String keyword
    ) {
        return chatSessionMapper.selectList(
                new QueryWrapper<ChatSession>()
                        .eq("user_id", userId)
                        .like("title", keyword)
                        .orderByDesc("last_time")
        );
    }

    @DeleteMapping("/{sessionId}")
    public Map<String, Object> delete(
            @PathVariable String sessionId,
            @RequestParam Long userId
    ) {

        chatSessionMapper.delete(
                new QueryWrapper<ChatSession>()
                        .eq("user_id", userId)
                        .eq("session_id", sessionId)
        );

        qaRecordMapper.delete(
                new QueryWrapper<QaRecord>()
                        .eq("user_id", userId)
                        .eq("session_id", sessionId)
        );

        return Map.of(
                "code", 200,
                "msg", "删除成功"
        );
    }
}
