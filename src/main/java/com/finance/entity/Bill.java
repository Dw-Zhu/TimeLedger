package com.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("finance_bill")
public class Bill {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long accountId;
    private Long categoryId;
    private Integer type; // 1-收入, 2-支出
    private BigDecimal amount;
    private String remark;
    private LocalDate billDate; // 记账日期（HTML5 前端日历选择绑定）
    private LocalDateTime createTime;
}