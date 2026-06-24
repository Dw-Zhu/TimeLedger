package com.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("finance_target")
public class Target {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private Integer termType; // 1-短期, 2-中期, 3-长期
    private LocalDate endDate;
    private LocalDateTime createTime;
}