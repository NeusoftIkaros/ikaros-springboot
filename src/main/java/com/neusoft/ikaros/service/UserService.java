package com.neusoft.ikaros.service;

import com.neusoft.ikaros.dto.LoginRequestDTO;
import com.neusoft.ikaros.dto.RegisterRequestDTO;
import com.neusoft.ikaros.entity.UserInfo;

public interface UserService {

    boolean register(RegisterRequestDTO dto);

    UserInfo login(LoginRequestDTO dto);

    boolean updatePassword(Long userId, String oldPassword, String newPassword);
}