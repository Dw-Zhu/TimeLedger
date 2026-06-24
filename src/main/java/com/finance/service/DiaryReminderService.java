package com.finance.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.entity.DiaryReminder;

import java.time.LocalDate;

public interface DiaryReminderService extends IService<DiaryReminder> {
    void syncDiaryReminder(DiaryReminder diaryReminder, Long userId);

    DiaryReminder selectByRecordDate(Long currentUserId, LocalDate recordDate);
}