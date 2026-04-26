package com.neusoft.ikaros.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.ikaros.entity.ChatSession;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {
}