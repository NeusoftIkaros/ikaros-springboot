package com.neusoft.ikaros.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.ikaros.dto.ChatRequestDTO;
import com.neusoft.ikaros.entity.QaRecord;
import com.neusoft.ikaros.mapper.QaRecordMapper;
import com.neusoft.ikaros.service.QaService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private QaService qaService;

    @Autowired
    private QaRecordMapper qaRecordMapper;

    @GetMapping("/send")
    public String send(
            @RequestParam Long userId,
            @RequestParam String sessionId,
            @RequestParam String question,
            @RequestParam String mode
    ) {
        return qaService.chat(new ChatRequestDTO(userId, question, mode, sessionId));
    }

    @GetMapping("/history")
    public List<QaRecord> history(
            @RequestParam Long userId,
            @RequestParam String sessionId
    ) {
        return qaRecordMapper.selectList(
                new QueryWrapper<QaRecord>()
                        .eq("user_id", userId)
                        .eq("session_id", sessionId)
                        .orderByAsc("created_at")
        );
    }

    @GetMapping("/search")
    public List<QaRecord> search(
            @RequestParam Long userId,
            @RequestParam String keyword
    ) {
        return qaRecordMapper.selectList(
                new QueryWrapper<QaRecord>()
                        .eq("user_id", userId)
                        .and(w -> w.like("question", keyword)
                                .or()
                                .like("answer", keyword))
                        .orderByDesc("created_at")
        );
    }

    @DeleteMapping("/message/{id}")
    public Map<String, Object> deleteMessage(@PathVariable Long id) {

        qaRecordMapper.deleteById(id);

        return Map.of(
                "code", 200,
                "msg", "删除成功"
        );
    }

    @GetMapping("/export")
    public void export(
            @RequestParam Long userId,
            @RequestParam String sessionId,
            HttpServletResponse response
    ) throws Exception {

        List<QaRecord> list = qaRecordMapper.selectList(
                new QueryWrapper<QaRecord>()
                        .eq("user_id", userId)
                        .eq("session_id", sessionId)
                        .orderByAsc("created_at")
        );

        StringBuilder sb = new StringBuilder();

        for (QaRecord r : list) {
            sb.append("Q: ").append(r.getQuestion()).append("\n");
            sb.append("A: ").append(r.getAnswer()).append("\n\n");
        }

        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=chat_" + sessionId + ".txt");

        response.getWriter().write(sb.toString());
    }
}
