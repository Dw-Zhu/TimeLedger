package com.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.entity.Account;
import com.finance.entity.Bill;
import com.finance.mapper.BillMapper;
import com.finance.service.AccountService;
import com.finance.service.BillService;
import com.finance.vo.BillVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    private final AccountService accountService;

    @Override
    @Transactional(rollbackFor = Exception.class) // 开启数据库事务，确保原子性
    public void executeBookkeeping(Bill bill, Long userId) {
        // 1. 获取对应关联账户并加锁保护
        Account account = accountService.getById(bill.getAccountId());
        if (account == null || !account.getUserId().equals(userId)) {
            throw new RuntimeException("关联资产账户不存在，同步中断");
        }

        // 2. 依据账单收支类型（1-收入，2-支出）扣减或增加账户实时余额
        if (bill.getType() == 1) {
            account.setBalance(account.getBalance().add(bill.getAmount()));
        } else if (bill.getType() == 2) {
            account.setBalance(account.getBalance().subtract(bill.getAmount()));
        } else {
            throw new RuntimeException("未知的流向类型");
        }

        // 3. 更新账户最新余额资产状态
        account.setUpdateTime(LocalDateTime.now());
        accountService.updateById(account);

        // 4. 保存当前账单明细流水记录
        bill.setUserId(userId);
        bill.setCreateTime(LocalDateTime.now());
        this.save(bill);
    }

    @Override
    public List<BillVo> getUserBills(Long userId) {
        return baseMapper.selectUserBillDetails(userId);
    }
}