package com.project01.skillineserver.mapper;

import com.project01.skillineserver.dto.reponse.CategoryResponse;
import com.project01.skillineserver.entity.CategoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {


    @Value("${domain.server}")
    private String DOMAIN_SERVER;

    public CategoryResponse toCategoriesResponse(CategoryEntity category) {

        String urlThumbnail = "";

        if(category.getPath()!=null){
            urlThumbnail = DOMAIN_SERVER+category.getPath();
        }

        return new CategoryResponse(category.getId(), category.getName(), urlThumbnail);
    }
}
