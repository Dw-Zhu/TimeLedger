package com.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("finance_budget")
public class Budget {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String month; // 格式: YYYY-MM
    private BigDecimal budgetAmount;
    private BigDecimal incomeTarget;
    private LocalDateTime createTime;
}