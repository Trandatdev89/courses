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

    @Value("${upload.directory.image}")
    private String PATH;

    public LectureResponse toLectureResponse(LectureEntity lectureEntity) {

        CourseProjection courseProjection = lectureRepository.getCourseWithCategory(lectureEntity.getCourseId());

        return LectureResponse.builder()
                .id(lectureEntity.getId())
                .processStatus(lectureEntity.getProcessStatus())
                .CategoryName(courseProjection.getCategoryName())
                .title(lectureEntity.getTitle())
                .urlThumbnail(DOMAIN_SERVER+PATH+"/"+lectureEntity.getImage())
                .duration(lectureEntity.getDuration()+" hours")
                .urlVideo(DOMAIN_SERVER+lectureEntity.getFilePath())
                .position(lectureEntity.getPosition())
                .createAt(dateUtil.format(lectureEntity.getCreatedAt()))
                .updateAt(dateUtil.format(lectureEntity.getUpdatedAt()))
                .build();
    }
}
