package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.Category;
import com.youcai.guest.repository.CategoryRepository;
import com.youcai.guest.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category findOne(String code) {
        return categoryRepository.findOne(code);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Map<String, String> findAllInMap() {
        List<Category> categories = categoryRepository.findAll();
        Map<String, String> categoryMap = new HashMap<>();
        for (Category category : categories){
            categoryMap.put(category.getCode(), category.getName());
        }
        return categoryMap;
    }
}
