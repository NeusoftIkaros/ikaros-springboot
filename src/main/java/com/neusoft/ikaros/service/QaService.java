package com.neusoft.ikaros.service;

import com.neusoft.ikaros.dto.ChatRequestDTO;
import com.neusoft.ikaros.entity.QaRecord;
import java.util.List;

public interface QaService {

    String chat(ChatRequestDTO dto);

    List<QaRecord> search(Long userId, String keyword);
}