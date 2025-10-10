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

    public CategoryResponse toLectureResponse(CategoryEntity category) {

        String normalizedPath = category.getPath()
                .replace("\\", "/"); // chuyển tất cả \ thành /

        // Thêm "/" nếu thiếu ở đầu
        if (!normalizedPath.startsWith("/")) {
            normalizedPath = "/" + normalizedPath;
        }

        String urlThumbnail = DOMAIN_SERVER+normalizedPath;

        return new CategoryResponse(category.getId(), category.getName(), urlThumbnail);
    }
}
