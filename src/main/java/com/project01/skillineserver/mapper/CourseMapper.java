package com.project01.skillineserver.mapper;

import com.project01.skillineserver.dto.reponse.CategoryResponse;
import com.project01.skillineserver.dto.reponse.CourseResponse;
import com.project01.skillineserver.entity.CategoryEntity;
import com.project01.skillineserver.entity.CourseEntity;
import com.project01.skillineserver.repository.CategoryRepository;
import com.project01.skillineserver.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    @Value("${domain.server}")
    private String DOMAIN_SERVER;

    @Autowired
    private CategoryRepository category;

    @Autowired
    private DateUtil dateUtil;

    public CourseResponse toLectureResponse(CourseEntity courseEntity) {

        String normalizedPath = courseEntity.getThumbnail_url()
                .replace("\\", "/"); // chuyển tất cả \ thành /

        // Thêm "/" nếu thiếu ở đầu
        if (!normalizedPath.startsWith("/")) {
            normalizedPath = "/" + normalizedPath;
        }

        String urlThumbnail = DOMAIN_SERVER+normalizedPath;

        return CourseResponse.builder()
                .id(courseEntity.getId())
                .title(courseEntity.getTitle())
                .thumbnail_url(urlThumbnail)
                .categoryName(category.findById(courseEntity.getCategoryId()).get().getName())
                .level(courseEntity.getLevel())
                .price(courseEntity.getPrice())
                .status(courseEntity.isStatus())
                .createAt(dateUtil.format(courseEntity.getCreateAt()))
                .updateAt(dateUtil.format(courseEntity.getUpdateAt()))
                .build();
    }
}
