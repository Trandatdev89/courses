package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.dto.reponse.CategoryResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.CategoryReq;
import com.project01.skillineserver.entity.CategoryEntity;
import com.project01.skillineserver.entity.OrderEntity;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.enums.FileType;
import com.project01.skillineserver.enums.SortField;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.mapper.CategoryMapper;
import com.project01.skillineserver.repository.CategoryRepository;
import com.project01.skillineserver.service.CategoryService;
import com.project01.skillineserver.utils.UploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UploadUtil uploadUtil;
    private final CategoryMapper categoryMapper;

    @Override
    public void save(CategoryReq category) throws IOException {
        CategoryEntity categoryInDB;

        if (category.id() != null) { //updatr
            categoryInDB = categoryRepository.findById(category.id())
                    .orElseGet(CategoryEntity::new);
        } else {  //create
            categoryInDB = new CategoryEntity();
        }

        String pathImage;

        if(category.path() != null && category.path() instanceof MultipartFile){
            pathImage = uploadUtil.createPathFile(category.path(), FileType.IMAGE).toString();
        }else{
            pathImage = categoryInDB.getPath();
        }

        categoryInDB.setName(category.name());
        categoryInDB.setPath(pathImage);
        categoryRepository.save(categoryInDB);
    }

    @Override
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toLectureResponse).toList();
    }

    @Override
    public PageResponse<CategoryResponse> getCategoryPagination(int page, int size, String sort, String keyword) {
        Sort sortField =  Sort.by(Sort.Direction.DESC,"createdAt");

        if(sort!=null && keyword!=null){
            sortField = SortField.ASC.getValue().equalsIgnoreCase(sort)
                    ? Sort.by(Sort.Direction.ASC,keyword)
                    : Sort.by(Sort.Direction.DESC,keyword);
        }

        PageRequest pageRequest  = PageRequest.of(page-1, size,sortField);

        Page<CategoryEntity> orders = categoryRepository.findAll(pageRequest);

        List<CategoryResponse> list = orders.getContent().stream().map(categoryMapper::toLectureResponse).toList();

        return PageResponse.<CategoryResponse>builder()
                .list(list)
                .page(page)
                .size(size)
                .totalElements(orders.getTotalElements())
                .totalPages(orders.getTotalPages())
                .build();
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
