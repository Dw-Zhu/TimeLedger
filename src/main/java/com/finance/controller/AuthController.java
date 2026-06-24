package com.finance.controller;

import com.finance.common.Result;
import com.finance.dto.LoginRequest;
import com.finance.dto.RegisterRequest;
import com.finance.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 1. 用户注册接口
    @PostMapping("/register")
    public Result<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            userService.register(registerRequest);
            return Result.success("邮箱验证成功，注册已完成！");
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    // 2. 用户登录接口
    @PostMapping("/login")
    public Result<Map<String, String>> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.login(loginRequest);
            // 将 Token 包装成 Key-Value 形式响应给前端存储（如 localStorage）
            return Result.success("登录成功，多端云数据已同步", Map.of("token", "Bearer " + token));
        } catch (RuntimeException e) {
            return Result.error(401, e.getMessage());
        }
    }
}