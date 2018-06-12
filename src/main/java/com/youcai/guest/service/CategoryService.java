package com.youcai.guest.service;

import com.youcai.guest.dataobject.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    Category findOne(String code);

    List<Category> findAll();

    Map<String, String> findAllInMap();
}
