package com.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.entity.Budget;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BudgetMapper extends BaseMapper<Budget> {
    Budget selectByMonth(@Param("userId") Long userId, @Param("month") String month);
    int insertBudget(Budget budget);
    int updateBudget(Budget budget);
}