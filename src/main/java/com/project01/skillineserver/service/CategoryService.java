package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.reponse.CategoryResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.CategoryReq;
import com.project01.skillineserver.entity.CategoryEntity;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    void save(CategoryReq category) throws IOException;
    void delete(List<Long> listCateId);
    List<CategoryResponse> getCategories();
    PageResponse<CategoryResponse> getCategoryPagination(int page, int size, String sort, String keyword);
}
