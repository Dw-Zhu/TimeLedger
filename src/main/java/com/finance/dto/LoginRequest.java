package com.finance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {


    @Email(message = "请输入标准的邮箱格式")
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;
}