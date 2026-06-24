package com.finance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "邮箱账号不能为空")
    @Email(message = "请输入标准的邮箱格式")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 15, message = "密码必须为 6 到 15 位字母或数字的组合")
    private String password;

    @NotBlank(message = "用户昵称不能为空")
    @Size(max = 50, message = "昵称长度不能超过 50 位")
    private String nickname;
}