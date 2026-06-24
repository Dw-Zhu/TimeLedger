package com.finance.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.entity.Account;
import java.util.List;

public interface AccountService extends IService<Account> {
    void addAccount(Account account, Long userId);
    List<Account> getUserAccounts(Long userId);
}