package com.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.entity.DiaryReminder;
import com.finance.mapper.DiaryReminderMapper;
import com.finance.service.DiaryReminderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DiaryReminderServiceImpl extends ServiceImpl<DiaryReminderMapper, DiaryReminder> implements DiaryReminderService {

    @Override
    public void syncDiaryReminder(DiaryReminder diaryReminder, Long userId) {
        diaryReminder.setUserId(userId);

        // 1. 调用 XML 中的传统 selectByRecordDate，检查当天是否已有日记/提醒
        DiaryReminder exist = baseMapper.selectByRecordDate(userId, diaryReminder.getRecordDate());

        diaryReminder.setUpdateTime(LocalDateTime.now());
        if (exist != null) {
            // 2. 存在则将主键 ID 锁死给新对象，调用 XML 的动态 <set> 传统 update 标签覆盖
            diaryReminder.setId(exist.getId());
            baseMapper.updateDiaryReminder(diaryReminder);
        } else {
            // 3. 不存在则设置初始创建时间，调用 XML 的传统 insert 插入落库
            diaryReminder.setCreateTime(LocalDateTime.now());
            baseMapper.insertDiaryReminder(diaryReminder);
        }
    }

    @Override
    public DiaryReminder selectByRecordDate(Long currentUserId, LocalDate recordDate) {
        return baseMapper.selectByRecordDate(currentUserId, recordDate);
    }
}