package com.project01.skillineserver.mapper;

import com.project01.skillineserver.dto.reponse.LectureResponse;
import com.project01.skillineserver.entity.LectureEntity;
import com.project01.skillineserver.projection.CourseProjection;
import com.project01.skillineserver.repository.LectureRepository;
import com.project01.skillineserver.utils.DateUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LectureMapper {

    private final DateUtil dateUtil;
    private final LectureRepository lectureRepository;

    @Value("${domain.server}")
    private String DOMAIN_SERVER;

    public LectureResponse toLectureResponse(LectureEntity lectureEntity) {

        CourseProjection courseProjection = lectureRepository.getCourseWithCategory(lectureEntity.getCourseId());

        String urlThumbnail = DOMAIN_SERVER+lectureEntity.getImage();

        String duration = lectureEntity.getDuration()+" hours";

        return LectureResponse.builder()
                .id(lectureEntity.getId())
                .courseName(courseProjection.getTitle())
                .CategoryName(courseProjection.getCategoryName())
                .title(lectureEntity.getTitle())
                .urlThumbnail(urlThumbnail)
                .duration(duration)
                .position(lectureEntity.getPosition())
                .createAt(dateUtil.format(lectureEntity.getCreatedAt()))
                .updateAt(dateUtil.format(lectureEntity.getUpdatedAt()))
                .build();
    }
}
