package com.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.entity.Category;
import com.finance.mapper.CategoryMapper;
import com.finance.service.CategoryService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public List<Category> getCategories(Long userId) {
        // 返回系统预设分类 (userId=0) 以及用户自己定义的个性化分类
        return lambdaQuery().eq(Category::getUserId, 0L).or().eq(Category::getUserId, userId).list();
    }
}