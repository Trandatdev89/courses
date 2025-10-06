package com.project01.skillineserver.controller;

import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.request.CourseReq;
import com.project01.skillineserver.entity.CourseEntity;
import com.project01.skillineserver.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/course")
public class CourseController {

    private final CourseService courseService;

    @PostMapping(value = "/save")
    public ApiResponse<CourseEntity> saveCourse(@ModelAttribute CourseReq courseReq) {
        return ApiResponse.<CourseEntity>builder()
                .code(200)
                .message("Success")
                .data(courseService.save(courseReq))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<CourseEntity>> getCourseByCategory(@RequestParam Long categoryId) {
        return ApiResponse.<List<CourseEntity>>builder()
                .code(200)
                .message("Success")
                .data(courseService.getAllByCategoryId(categoryId))
                .build();
    }

    @DeleteMapping(value = "/{ids}")
    public ApiResponse<?> deleteCourse(@PathVariable List<Long> ids) {
        courseService.delete(ids);
        return ApiResponse.builder()
                .code(200)
                .message("Success")
                .build();
    }

    @GetMapping(value = "/list/{ids}")
    public ApiResponse<?> getListCourseById(@PathVariable List<String> ids) {
        return ApiResponse.builder()
                .code(200)
                .data(courseService.getListCourseById(ids))
                .message("Success")
                .build();
    }

    @GetMapping(value = "/{id}")
    public ApiResponse<CourseEntity> getCourseById(@PathVariable Long id) {
        return ApiResponse.<CourseEntity>builder()
                .code(200)
                .message("Success")
                .data(courseService.getCourseById(id))
                .build();
    }

}
