package com.neusoft.ikaros.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.ikaros.dto.LoginRequestDTO;
import com.neusoft.ikaros.dto.RegisterRequestDTO;
import com.neusoft.ikaros.entity.UserInfo;
import com.neusoft.ikaros.mapper.UserMapper;
import com.neusoft.ikaros.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean register(RegisterRequestDTO dto) {
        UserInfo exist = userMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUsername, dto.getUsername())
        );

        if (exist != null) {
            return false;
        }

        UserInfo user = new UserInfo();
        user.setUsername(dto.getUsername());

        String md5 = DigestUtils.md5DigestAsHex(
                dto.getPassword().getBytes(StandardCharsets.UTF_8)
        );

        user.setPasswordHash(md5);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.insert(user);
        return true;
    }

    @Override
    public UserInfo login(LoginRequestDTO dto) {

        String md5 = DigestUtils.md5DigestAsHex(
                dto.getPassword().getBytes(StandardCharsets.UTF_8)
        );

        return userMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUsername, dto.getUsername())
                        .eq(UserInfo::getPasswordHash, md5)
        );
    }

    @Override
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {

        UserInfo user = userMapper.selectById(userId);

        if (user == null) {
            return false;
        }

        String oldMd5 = DigestUtils.md5DigestAsHex(
                oldPassword.getBytes(StandardCharsets.UTF_8)
        );

        if (!oldMd5.equals(user.getPasswordHash())) {
            return false;
        }

        if (oldPassword.equals(newPassword)) {
            return false;
        }

        String newMd5 = DigestUtils.md5DigestAsHex(
                newPassword.getBytes(StandardCharsets.UTF_8)
        );

        user.setPasswordHash(newMd5);
        user.setUpdatedAt(LocalDateTime.now());

        return userMapper.updateById(user) > 0;
    }
}
