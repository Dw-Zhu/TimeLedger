package com.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.dto.LoginRequest;
import com.finance.dto.RegisterRequest;
import com.finance.entity.User;

public interface UserService extends IService<User> {

    // 处理用户注册业务
    void register(RegisterRequest registerRequest);

    // 处理用户登录业务，登录成功返回可用于多端同步的 JWT 令牌
    String login(LoginRequest loginRequest);
}