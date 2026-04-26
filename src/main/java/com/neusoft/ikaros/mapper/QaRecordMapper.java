package com.neusoft.ikaros.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.ikaros.entity.QaRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QaRecordMapper extends BaseMapper<QaRecord> {

    @Select("""
        SELECT *
        FROM qa_record
        WHERE user_id = #{userId}
          AND session_id = #{sessionId}
        ORDER BY created_at ASC
    """)
    List<QaRecord> selectBySessionId(Long userId, String sessionId);

    @Select("""
        SELECT *
        FROM qa_record
        WHERE user_id = #{userId}
          AND (question LIKE CONCAT('%', #{keyword}, '%')
               OR answer LIKE CONCAT('%', #{keyword}, '%'))
        ORDER BY created_at DESC
    """)
    List<QaRecord> searchByKeyword(Long userId, String keyword);

    @Select("""
        SELECT MAX(created_at)
        FROM qa_record
        WHERE user_id = #{userId}
          AND session_id = #{sessionId}
    """)
    java.time.LocalDateTime getLastTime(Long userId, String sessionId);
}