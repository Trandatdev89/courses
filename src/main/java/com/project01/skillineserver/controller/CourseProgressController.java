package com.project01.skillineserver.controller;

import com.project01.skillineserver.config.CustomUserDetail;
import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.entity.CourseProgressEntity;
import com.project01.skillineserver.projection.CourseProgressProjection;
import com.project01.skillineserver.repository.CourseProgressRepository;
import com.project01.skillineserver.service.CourseProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = "/api/progress")
@RequiredArgsConstructor
public class CourseProgressController {

    private final CourseProgressService courseProgressService;
    private final CourseProgressRepository courseProgressRepository;

    @GetMapping
    public ApiResponse<List<CourseProgressEntity> > checkProgressCourse(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                                     @RequestParam Long enrollmentId){
        return ApiResponse.<List<CourseProgressEntity>>builder()
                .code(200)
                .message("Success!")
                .data(courseProgressService.checkProgressCourseProgressProjection(customUserDetail.getUser().getId(),enrollmentId))
                .build();
    }

    @PostMapping
    public ApiResponse<?> saveProgress(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                                        @RequestParam String lectureId,
                                                                        @RequestParam Long enrollmentId){

        courseProgressRepository.save(CourseProgressEntity.builder()
                        .lectureId(lectureId)
                        .isCompleted(true)
                        .completedAt(Instant.now())
                        .enrollmentId(enrollmentId)
                .build());

        return ApiResponse.builder()
                .code(200)
                .message("update Success!")
                .build();
    }
}
