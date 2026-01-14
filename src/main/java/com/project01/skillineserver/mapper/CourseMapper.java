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

import java.io.File;

@Component
public class CourseMapper {

    @Value("${domain.server}")
    private String DOMAIN_SERVER;

    @Autowired
    private CategoryRepository category;

    @Autowired
    private DateUtil dateUtil;

    public CourseResponse toLectureResponse(CourseEntity courseEntity) {

        String path = courseEntity.getThumbnail_url().replace(File.separator, "/");

        return CourseResponse.builder()
                .id(courseEntity.getId())
                .title(courseEntity.getTitle())
                .thumbnail_url(DOMAIN_SERVER+path)
                .categoryName(courseEntity.getCategoryId()!=null? category.findById(courseEntity.getCategoryId()).get().getName() : null )
                .level(courseEntity.getLevel())
                .price(courseEntity.getPrice())
                .description(courseEntity.getDescription())
                .status(courseEntity.isStatus())
                .createAt(dateUtil.format(courseEntity.getCreatedAt()))
                .updateAt(dateUtil.format(courseEntity.getUpdatedAt()))
                .build();
    }
}
