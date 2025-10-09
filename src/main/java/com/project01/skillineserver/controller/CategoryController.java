package com.project01.skillineserver.controller;

import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.CategoryReq;
import com.project01.skillineserver.entity.CategoryEntity;
import com.project01.skillineserver.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<?> save(@ModelAttribute CategoryReq categoryReq) throws IOException {
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

    @GetMapping(value = "/pagination")
    public ApiResponse<PageResponse<CategoryEntity>> getCategoryPagination(@RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size,
                                                                           @RequestParam(required = false) String sort,
                                                                           @RequestParam(required = false) String keyword){
        return ApiResponse.<PageResponse<CategoryEntity>>builder()
                .message("Save Category")
                .data(categoryService.getCategoryPagination(page,size,sort,keyword))
                .code(200)
                .build();
    }
}
