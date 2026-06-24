package com.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("user_diary_reminder")
public class DiaryReminder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String diaryContent;
    private String moodStatus;
    private String reminderTitle;
    private LocalDateTime reminderTime;
    private BigDecimal reminderThreshold;
    private Integer isReminded; // 0-否, 1-是
    private LocalDate recordDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}