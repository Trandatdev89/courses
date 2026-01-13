package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.entity.CourseProgressEntity;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.repository.CourseProgressRepository;
import com.project01.skillineserver.repository.EnrollmentRepository;
import com.project01.skillineserver.repository.LectureRepository;
import com.project01.skillineserver.service.CourseProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseProgressServiceImpl implements CourseProgressService {

    private final CourseProgressRepository courseProgressRepository;
    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional(rollbackFor = {AppException.class})
    public void updateProgressCourse(Long enrollmentId, String lectureId) {

        if(!lectureRepository.existsById(lectureId)){
            throw new AppException(ErrorCode.LECTURE_NOT_FOUND);
        }

        if(!enrollmentRepository.existsById(enrollmentId)){
            throw new AppException(ErrorCode.USER_NOT_BUY_COURSE);
        }

        courseProgressRepository.save(CourseProgressEntity.builder()
                        .enrollmentId(enrollmentId)
                        .completedAt(Instant.now())
                        .isCompleted(true)
                        .lectureId(lectureId)
                .build());
    }

    @Override
    public List<CourseProgressEntity> checkProgressCourseProgressProjection(Long userId, Long enrollment) {
        return courseProgressRepository.getProgressUserInOnCourse(userId,enrollment);
    }

}
