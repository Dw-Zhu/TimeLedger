package com.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.entity.DiaryReminder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;

@Mapper
public interface DiaryReminderMapper extends BaseMapper<DiaryReminder> {
    DiaryReminder selectByRecordDate(@Param("userId") Long userId, @Param("recordDate") LocalDate recordDate);
    int insertDiaryReminder(DiaryReminder diaryReminder);
    int updateDiaryReminder(DiaryReminder diaryReminder);
}