package com.finance.controller;

import com.finance.common.Result;
import com.finance.entity.Account;
import com.finance.entity.Category;
import com.finance.entity.Bill;
import com.finance.service.AccountService;
import com.finance.service.CategoryService;
import com.finance.service.BillService;
import com.finance.vo.BillVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final AccountService accountService;
    private final CategoryService categoryService;
    private final BillService billService;

    // 辅助私有方法：获取当前通过 JWT 链条鉴权通过的用户 ID
    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // --- 账户管理相关端点 ---
    @PostMapping("/account/add")
    public Result<String> createAccount(@RequestBody Account account) {
        try {
            accountService.addAccount(account, getCurrentUserId());
            return Result.success("资产账户创建成功");
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/account/list")
    public Result<List<Account>> listAccounts() {
        return Result.success(accountService.getUserAccounts(getCurrentUserId()));
    }

    // --- 分类管理相关端点 ---
    @GetMapping("/category/list")
    public Result<List<Category>> listCategories() {
        return Result.success(categoryService.getCategories(getCurrentUserId()));
    }

    // --- 核心收支记账账单端点 ---
    @PostMapping("/bill/book")
    public Result<String> addBillRecord(@RequestBody Bill bill) {
        try {
            billService.executeBookkeeping(bill, getCurrentUserId());
            return Result.success("记账保存成功，云端数据已安全刷新");
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result<List<BillVo>> listBills() {
        // 这里 billService.getUserBills 返回的是 List<BillVo>
        // 对应方法签名的 Result<List<BillVo>>，类型完美对齐
        return Result.success(billService.getUserBills(getCurrentUserId()));
    }
}