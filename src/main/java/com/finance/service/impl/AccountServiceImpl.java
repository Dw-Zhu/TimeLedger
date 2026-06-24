package com.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.entity.Account;
import com.finance.mapper.AccountMapper;
import com.finance.service.AccountService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    @Override
    public void addAccount(Account account, Long userId) {
        // 相同的账户名称只能添加一次（如同一个用户下不能同时加两个“招商银行卡”）
        Long count = lambdaQuery().eq(Account::getUserId, userId).eq(Account::getName, account.getName()).count();
        if (count > 0) {
            throw new RuntimeException("相同名称的资产账户已存在，请勿重复添加");
        }
        account.setUserId(userId);
        account.setCreateTime(LocalDateTime.now());
        account.setUpdateTime(LocalDateTime.now());
        this.save(account);
    }

    @Override
    public List<Account> getUserAccounts(Long userId) {
        return lambdaQuery().eq(Account::getUserId, userId).orderByDesc(Account::getBalance).list();
    }
}