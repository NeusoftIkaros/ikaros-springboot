package com.neusoft.ikaros.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 6, max = 12, message = "用户名长度必须为6-12位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能小于6位")
    private String password;
}