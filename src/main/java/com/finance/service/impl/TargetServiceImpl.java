package com.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.entity.Target;
import com.finance.mapper.TargetMapper;
import com.finance.service.TargetService;
import org.springframework.stereotype.Service;

@Service // 👈 关键注解：向 Spring 容器正式注册当前 Bean
public class TargetServiceImpl extends ServiceImpl<TargetMapper, Target> implements TargetService {
}