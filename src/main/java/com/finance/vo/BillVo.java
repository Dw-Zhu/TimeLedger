package com.finance.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BillVo {
    private Long id;
    private Integer type; // 1-收入, 2-支出
    private BigDecimal amount;
    private String remark;
    private LocalDate billDate;

    // 联查字段
    private String accountName;  // 对应资产账户名，如：招商银行卡
    private String categoryName; // 对应分类名，如：餐饮美食
}