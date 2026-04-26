package com.neusoft.ikaros.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.ikaros.entity.QaRecord;
import com.neusoft.ikaros.mapper.QaRecordMapper;
import com.neusoft.ikaros.service.QaRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QaRecordServiceImpl implements QaRecordService {

    @Autowired
    private QaRecordMapper mapper;

    @Override
    public List<QaRecord> listByUser(Long userId) {
        return mapper.selectList(
                new LambdaQueryWrapper<QaRecord>()
                        .eq(QaRecord::getUserId, userId)
                        .orderByDesc(QaRecord::getId)
        );
    }

    @Override
    public List<QaRecord> search(Long userId, String keyword) {
        return mapper.selectList(
                new LambdaQueryWrapper<QaRecord>()
                        .eq(QaRecord::getUserId, userId)
                        .and(w -> w.like(QaRecord::getQuestion, keyword)
                                .or()
                                .like(QaRecord::getAnswer, keyword))
                        .orderByDesc(QaRecord::getId)
        );
    }

    @Override
    public boolean deleteOne(Long id) {
        return mapper.deleteById(id) > 0;
    }

    @Override
    public String exportText(Long userId) {
        List<QaRecord> list = listByUser(userId);

        StringBuilder sb = new StringBuilder();

        for (QaRecord r : list) {
            sb.append("Q: ").append(r.getQuestion()).append("\n");
            sb.append("A: ").append(r.getAnswer()).append("\n");
            sb.append("-------------------------\n");
        }

        return sb.toString();
    }
}