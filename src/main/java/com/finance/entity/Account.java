package com.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("finance_account")
public class Account {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String name;
    private BigDecimal balance;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}