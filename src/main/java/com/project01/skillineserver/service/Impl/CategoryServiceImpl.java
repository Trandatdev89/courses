package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.dto.reponse.CategoryResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.CategoryReq;
import com.project01.skillineserver.entity.CategoryEntity;
import com.project01.skillineserver.entity.CourseEntity;
import com.project01.skillineserver.entity.OrderEntity;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.enums.FileType;
import com.project01.skillineserver.enums.SortField;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.mapper.CategoryMapper;
import com.project01.skillineserver.repository.CategoryRepository;
import com.project01.skillineserver.repository.CourseRepository;
import com.project01.skillineserver.service.CategoryService;
import com.project01.skillineserver.utils.MapUtil;
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
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UploadUtil uploadUtil;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(rollbackFor = AppException.class)
    public void save(CategoryReq category) throws IOException {
        CategoryEntity categoryInDB = Optional.ofNullable(category.id())
                .flatMap(categoryRepository::findById)
                .orElse(new CategoryEntity());

        String pathImage = resolveImagePath(category.path(),categoryInDB.getPath());

        categoryInDB.setName(category.name());
        categoryInDB.setPath(pathImage);
        categoryInDB.setActive(true);

        categoryRepository.save(categoryInDB);
    }

    @Override
    public PageResponse<CategoryResponse> getCategoryPagination(int page, int size, String sort, String keyword) {
        Sort sortField = MapUtil.parseSort(sort);
        PageRequest pageRequest  = PageRequest.of(page-1, size,sortField);

        Page<CategoryEntity> pageCategories = categoryRepository.getCategories(keyword,pageRequest);

        List<CategoryResponse> list = pageCategories.getContent().stream().map(categoryMapper::toCategoriesResponse).toList();

        return PageResponse.<CategoryResponse>builder()
                .list(list)
                .page(page)
                .size(size)
                .totalElements(pageCategories.getTotalElements())
                .totalPages(pageCategories.getTotalPages())
                .build();
    }

    @Override
    @Transactional(rollbackFor = {AppException.class})
    public void delete(List<Long> categoryIds) {
        if(categoryIds == null || categoryIds.isEmpty()){
            throw new AppException(ErrorCode.LIST_ID_EMPTY);
        }

        categoryRepository.deleteCategoryByIds(categoryIds);
    }

    private String resolveImagePath(Object inputPath, String exitingPath) throws IOException {
        if(inputPath instanceof MultipartFile multipartFile){
            return uploadUtil.createPathFile(multipartFile,FileType.IMAGE).toString();
        }else{
            return exitingPath;
        }
    }

}
