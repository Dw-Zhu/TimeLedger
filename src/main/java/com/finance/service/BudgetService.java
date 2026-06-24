package com.finance.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.entity.Budget;
import java.math.BigDecimal;

public interface BudgetService extends IService<Budget> {
    void saveOrUpdateBudget(Budget budget, Long userId);
    BigDecimal getMonthExpenseStatus(Long userId, String month);
    Budget getBudgetByMonth(Long userId, String month);
}