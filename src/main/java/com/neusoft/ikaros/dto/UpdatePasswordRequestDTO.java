package com.neusoft.ikaros.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UpdatePasswordRequestDTO {

    @Setter
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @Setter
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, message = "新密码长度不能少于6位")
    private String newPassword;
}