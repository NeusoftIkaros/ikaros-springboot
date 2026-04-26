package com.neusoft.ikaros.controller;

import com.neusoft.ikaros.dto.LoginRequestDTO;
import com.neusoft.ikaros.dto.RegisterRequestDTO;
import com.neusoft.ikaros.dto.UpdatePasswordRequestDTO;
import com.neusoft.ikaros.entity.UserInfo;
import com.neusoft.ikaros.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody @Valid RegisterRequestDTO dto) {
        boolean ok = userService.register(dto);

        Map<String, Object> res = new HashMap<>();
        if (ok) {
            res.put("code", 200);
            res.put("msg", "注册成功");
        } else {
            res.put("code", 400);
            res.put("msg", "账号已存在");
        }
        return res;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequestDTO dto) {
        UserInfo user = userService.login(dto);

        Map<String, Object> res = new HashMap<>();
        if (user != null) {
            res.put("code", 200);
            res.put("msg", "登录成功");
            res.put("userId", user.getId());
        } else {
            res.put("code", 400);
            res.put("msg", "账号或密码错误");
        }
        return res;
    }

    @PostMapping("/password/update")
    public Map<String, Object> updatePassword(@RequestBody @Valid UpdatePasswordRequestDTO dto) {

        boolean ok = userService.updatePassword(
                dto.getUserId(),
                dto.getOldPassword(),
                dto.getNewPassword()
        );

        Map<String, Object> res = new HashMap<>();
        if (ok) {
            res.put("code", 200);
            res.put("msg", "密码修改成功");
        } else {
            res.put("code", 400);
            res.put("msg", "旧密码错误或新旧密码相同");
        }
        return res;
    }
}
