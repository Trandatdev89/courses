package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.request.CategoryReq;
import com.project01.skillineserver.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {
    void save(CategoryReq category);
    void delete(List<Long> listCateId);
    List<CategoryEntity> getCategories();
}
