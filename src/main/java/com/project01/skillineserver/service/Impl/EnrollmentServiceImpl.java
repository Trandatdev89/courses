package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.entity.CourseEntity;
import com.project01.skillineserver.projection.CourseProjection;
import com.project01.skillineserver.repository.EnrollmentRepository;
import com.project01.skillineserver.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;


    @Override
    public List<CourseProjection> getListCourseUserBuy() {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        return enrollmentRepository.getListCourseUserBuy(userId);
    }

    @Override
    public Boolean checkUserEnrollment(Long courseId) {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        return enrollmentRepository.isUserEnrolledInCourse(userId,courseId) > 0;
    }
}
