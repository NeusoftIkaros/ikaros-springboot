package com.neusoft.ikaros.service;

import com.neusoft.ikaros.entity.QaRecord;

import java.util.List;

public interface QaRecordService {

    List<QaRecord> listByUser(Long userId);

    List<QaRecord> search(Long userId, String keyword);

    boolean deleteOne(Long id);

    String exportText(Long userId);
}