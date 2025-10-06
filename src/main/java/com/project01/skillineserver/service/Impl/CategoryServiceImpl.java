package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.dto.request.CategoryReq;
import com.project01.skillineserver.entity.CategoryEntity;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.repository.CategoryRepository;
import com.project01.skillineserver.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void save(CategoryReq category) {
        CategoryEntity categoryInDB;

        if (category.id() != null) {
            categoryInDB = categoryRepository.findById(category.id())
                    .orElseGet(CategoryEntity::new);
        } else {
            categoryInDB = new CategoryEntity();
        }
        categoryInDB.setName(category.name());
        categoryRepository.save(categoryInDB);
    }

    @Override
    public List<CategoryEntity> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = {AppException.class})
    public void delete(List<Long> categoryIds) {
        if(categoryIds!=null && !categoryIds.isEmpty()){
            categoryRepository.deleteByIdIn(categoryIds);
        }else{
            throw new  AppException(ErrorCode.LIST_ID_EMPTY);
        }
    }

}
