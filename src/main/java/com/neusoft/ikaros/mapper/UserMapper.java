package com.neusoft.ikaros.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.ikaros.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
}