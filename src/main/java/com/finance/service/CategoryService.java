package com.finance.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.entity.Category;
import java.util.List;

public interface CategoryService extends IService<Category> {
    List<Category> getCategories(Long userId);
}