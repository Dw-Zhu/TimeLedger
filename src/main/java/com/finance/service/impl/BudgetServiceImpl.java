package com.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.entity.Budget;
import com.finance.entity.Bill;
import com.finance.mapper.BudgetMapper;
import com.finance.service.BudgetService;
import com.finance.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl extends ServiceImpl<BudgetMapper, Budget> implements BudgetService {

    private final BillService billService;

    @Override
    public void saveOrUpdateBudget(Budget budget, Long userId) {
        budget.setUserId(userId);

        // 1. 调用 XML 中的传统 selectByMonth 查询该月是否已有预算记录
        Budget exist = baseMapper.selectByMonth(userId, budget.getMonth());

        if (exist != null) {
            // 2. 如果存在，将主键ID赋给新对象，调用 XML 中的传统 updateBudget 更新
            budget.setId(exist.getId());
            baseMapper.updateBudget(budget);
        } else {
            // 3. 如果不存在，设置创建时间，调用 XML 中的传统 insertBudget 插入
            budget.setCreateTime(LocalDateTime.now());
            baseMapper.insertBudget(budget);
        }
    }

    @Override
    public BigDecimal getMonthExpenseStatus(Long userId, String month) {
        // 统计当前用户在该月份下的所有支出(Type=2)总和，用于阈值对比
        return billService.lambdaQuery()
                .eq(Bill::getUserId, userId)
                .eq(Bill::getType, 2)
                .likeRight(Bill::getBillDate, month)
                .list()
                .stream()
                .map(Bill::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Budget getBudgetByMonth(Long userId, String month) {
        return baseMapper.selectByMonth(userId, month);
    }
}