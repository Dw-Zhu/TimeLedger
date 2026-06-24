package com.finance.controller;

import com.finance.common.Result;
import com.finance.entity.Budget;
import com.finance.entity.Target;
import com.finance.entity.DiaryReminder;
import com.finance.service.BudgetService;
import com.finance.service.DiaryReminderService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.service.TargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/advanced")
@RequiredArgsConstructor
public class AdvancedFinanceController {

    private final BudgetService budgetService;
    private final DiaryReminderService diaryReminderService;
    private final TargetService targetService;

    // 统一网关提取：从 Spring Security JWT 链条中提取当前登录者的唯一用户 ID
    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // ==================== (1) 月度预算控制中心 ====================

    // 保存或修改当前月份的支出预算限额/收入目标
    @PostMapping("/budget/save")
    public Result<String> saveBudget(@RequestBody Budget budget) {
        if (budget.getMonth() == null || !budget.getMonth().matches("^\\d{4}-\\d{2}$")) {
            return Result.error(400, "预算月份格式必须为 YYYY-MM");
        }
        budgetService.saveOrUpdateBudget(budget, getCurrentUserId());
        return Result.success("月度预算阈值已安全同步至云端");
    }

    // 获取当前月度预算、当前累计开销以及是否触发“超支报警阈值”
    @GetMapping("/budget/analysis")
    public Result<Map<String, Object>> getBudgetAnalysis(@RequestParam String month) {
        Long userId = getCurrentUserId();

        // 调用我们刚刚编写的 Service 层，内部正是通过 XML 执行的单条月份锁定查询
        Budget budget = budgetService.getBudgetByMonth(userId, month);
        // 计算当前月累计消费
        BigDecimal currentExpense = budgetService.getMonthExpenseStatus(userId, month);

        BigDecimal limit = budget != null ? budget.getBudgetAmount() : BigDecimal.ZERO;
        // 核心阈值判定：当月支出总和是否超过了设定的最大预算限额
        boolean isOver = limit.compareTo(BigDecimal.ZERO) > 0 && currentExpense.compareTo(limit) > 0;

        return Result.success(Map.of(
                "budgetLimit", limit,
                "currentExpense", currentExpense,
                "isOverBudget", isOver, // ⚠️ 关键布尔值：用于前端响应式自适应弹出超支红色警报
                "incomeTarget", budget != null ? budget.getIncomeTarget() : BigDecimal.ZERO
        ));
    }

    // ==================== (2) 心情日记与事件提醒管理 ====================

    // 用户在任意移动端或网页端编写/修改心情日记与触发事件，覆盖上传同步
    @PostMapping("/diary/sync")
    public Result<String> syncDiary(@RequestBody DiaryReminder diary) {
        if (diary.getRecordDate() == null) {
            diary.setRecordDate(LocalDate.now());
        }
        diaryReminderService.syncDiaryReminder(diary, getCurrentUserId());
        return Result.success("拾光日记与资产事项已同步备份完成");
    }

    // 获取特定日期的日记文本、心情状态和提醒事件
    @GetMapping("/diary/info")
    public Result<DiaryReminder> getDiaryInfo(@RequestParam String date) {
        LocalDate recordDate = LocalDate.parse(date);
        // 穿透执行 DiaryReminderMapper.xml 中的 selectByRecordDate 语句
        DiaryReminder info = diaryReminderService.selectByRecordDate(getCurrentUserId(), recordDate);
        return Result.success(info);
    }

    // 根据主键删除特定日期的心情日记记录
    @DeleteMapping("/diary/delete/{id}")
    public Result<String> deleteDiary(@PathVariable Long id) {
        DiaryReminder diary = diaryReminderService.getById(id);
        if (diary != null && diary.getUserId().equals(getCurrentUserId())) {
            diaryReminderService.removeById(id);
            return Result.success("该篇心情账目记录已成功从云端抹除");
        }
        return Result.error(403, "无权操作当前账户下的理财日记");
    }

    // ==================== (3) 梦想板目标计划 ====================
    @PostMapping("/target/save")
    public Result<String> saveTarget(@RequestBody Target target) {
        target.setUserId(getCurrentUserId());
        if (target.getId() != null) {
            targetService.updateById(target);
        } else {
            target.setCreateTime(java.time.LocalDateTime.now());
            targetService.save(target);
        }
        return Result.success("长中短期财富梦想目标更新成功");
    }

    @GetMapping("/target/list")
    public Result<List<Target>> listTargets() {
        return Result.success(targetService.lambdaQuery().eq(Target::getUserId, getCurrentUserId()).list());
    }
}