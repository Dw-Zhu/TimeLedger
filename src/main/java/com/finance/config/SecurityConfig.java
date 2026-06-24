package com.finance.config;

import com.finance.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 用于用户密码的 BCrypt 强哈希加密
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用多端同步不需要的 CSRF 保护，并禁用传统 Session 管理
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 2. 配置接口访问控制
                .authorizeHttpRequests(auth -> auth
                        // 💡 完美闭环白名单：一口气放行所有可能的登录、注册路径
                        .requestMatchers(
                                "/user/login", "/user/register",
                                "/api/user/login", "/api/user/register",
                                "/api/auth/login", "/api/auth/register",
                                "/error"
                        ).permitAll()
                        // 其他所有属于核心记账、日记、预算等接口必须携带有效 Token 访问
                        .anyRequest().authenticated()
                );

        // 3. 将我们的 JWT 过滤器插到 Spring Security 的账号密码验证过滤器之前
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}