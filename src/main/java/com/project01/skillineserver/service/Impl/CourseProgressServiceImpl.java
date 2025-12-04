package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.entity.CourseProgressEntity;
import com.project01.skillineserver.repository.CourseProgressRepository;
import com.project01.skillineserver.service.CourseProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CourseProgressServiceImpl implements CourseProgressService {

    private final CourseProgressRepository courseProgressRepository;

    @Override
    @Transactional
    public void updateProgressCourse(Long enrollmentId, String lectureId) {
        courseProgressRepository.save(CourseProgressEntity.builder()
                        .enrollmentId(enrollmentId)
                        .completedAt(Instant.now())
                        .isCompleted(true)
                        .lectureId(lectureId)
                .build());
    }
}
