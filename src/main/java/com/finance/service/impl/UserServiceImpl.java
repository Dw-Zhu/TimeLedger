package com.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.dto.LoginRequest;
import com.finance.dto.RegisterRequest;
import com.finance.entity.User;
import com.finance.mapper.UserMapper;
import com.finance.security.JwtTokenProvider;
import com.finance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    public void register(RegisterRequest request) {
        // 1. 严格检查邮箱唯一性 (同一个邮箱地址只能注册一个账号)
        Long count = lambdaQuery().eq(User::getEmail, request.getEmail()).count();
        if (count > 0) {
            throw new RuntimeException("该邮箱地址已被注册，请直接登录或更换邮箱");
        }

        // 2. 构造并填充用户实体，利用 BCrypt 对明文密码进行强哈希脱敏加密
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 3. 落库持久化
        this.save(user);
    }

    @Override
    public String login(LoginRequest request) {
        // 1. 依据邮箱账号拉取库中的用户信息
        User user = lambdaQuery().one();


        // 2. 匹配并校验密码是否正确 (BCrypt 内部包含盐值自动比对机制)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码输入错误，请重新输入");
        }

        // 3. 密码匹配成功，签发全局唯一的高效 JWT 令牌，供移动端/Web端多端同步使用
        return tokenProvider.generateToken(user.getId(), user.getEmail());
    }
}