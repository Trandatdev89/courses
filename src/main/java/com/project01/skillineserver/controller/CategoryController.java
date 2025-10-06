package com.project01.skillineserver.controller;

import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.request.CategoryReq;
import com.project01.skillineserver.entity.CategoryEntity;
import com.project01.skillineserver.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<?> save(@RequestBody CategoryReq categoryReq){
        categoryService.save(categoryReq);
        return ApiResponse.builder()
                .message("Save Category")
                .code(200)
                .build();
    }

    @DeleteMapping(value = "/{ids}")
    public ApiResponse<?> deleteCategory(@PathVariable List<Long> ids){
        categoryService.delete(ids);
        return ApiResponse.builder()
                .message("Save Category")
                .code(200)
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryEntity>> getCategories(){
        return ApiResponse.<List<CategoryEntity>>builder()
                .message("Save Category")
                .data(categoryService.getCategories())
                .code(200)
                .build();
    }
}
