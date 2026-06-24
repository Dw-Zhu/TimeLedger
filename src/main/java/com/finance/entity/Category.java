package com.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("finance_category")
public class Category {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId; // 0代表系统公共预设分类，大于0代表用户自定义分类
    private String name;
    private Integer type; // 1-收入, 2-支出
}