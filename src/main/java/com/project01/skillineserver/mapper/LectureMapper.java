package com.project01.skillineserver.mapper;

import com.project01.skillineserver.dto.reponse.LectureResponse;
import com.project01.skillineserver.entity.LectureEntity;
import com.project01.skillineserver.projection.CourseProjection;
import com.project01.skillineserver.repository.LectureRepository;
import com.project01.skillineserver.utils.DateUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LectureMapper {

    private final DateUtil dateUtil;
    private final LectureRepository lectureRepository;
    private final HttpServletRequest request;

    public LectureResponse toLectureResponse(LectureEntity lectureEntity) {

        String domain = request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort();

        CourseProjection courseProjection = lectureRepository.getCourseWithCategory(lectureEntity.getCourseId());

        String urlThumbnail = domain+lectureEntity.getImage();

        String duration = lectureEntity.getDuration()+" hours";

        return LectureResponse.builder()
                .id(lectureEntity.getId())
                .courseName(courseProjection.getTitle())
                .CategoryName(courseProjection.getCategoryName())
                .title(lectureEntity.getTitle())
                .urlThumbnail(urlThumbnail)
                .duration(duration)
                .position(lectureEntity.getPosition())
                .createAt(dateUtil.format(lectureEntity.getCreateAt()))
                .updateAt(dateUtil.format(lectureEntity.getUpdateAt()))
                .build();
    }
}
