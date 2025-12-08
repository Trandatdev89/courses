package com.project01.skillineserver.controller;

import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.reponse.CourseResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.CourseReq;
import com.project01.skillineserver.entity.CourseEntity;
import com.project01.skillineserver.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/course")
public class CourseController {

    private final CourseService courseService;

    @PostMapping(value = "/save")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ApiResponse<CourseEntity> saveCourse(@ModelAttribute CourseReq courseReq) throws IOException {
        return ApiResponse.<CourseEntity>builder()
                .code(200)
                .message("Success")
                .data(courseService.save(courseReq))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<CourseResponse>> getCourseByCategory(@RequestParam Long categoryId) {
        return ApiResponse.<List<CourseResponse>>builder()
                .code(200)
                .message("Success")
                .data(courseService.getAllByCategoryId(categoryId))
                .build();
    }

    @GetMapping(value = "/not-pagi")
    public ApiResponse<List<CourseResponse>> getCourseNotPagination() {
        return ApiResponse.<List<CourseResponse>>builder()
                .code(200)
                .message("Success")
                .data(courseService.getCourseNotPagination())
                .build();
    }

    @GetMapping(value = "/all")
    public ApiResponse<PageResponse<CourseResponse>> getCourses(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "1000") int size,
                                                              @RequestParam(required = false) String sort,
                                                              @RequestParam(required = false) String keyword) {
        return ApiResponse.<PageResponse<CourseResponse>>builder()
                .code(200)
                .message("Success")
                .data(courseService.getCourses(page,size,sort,keyword))
                .build();
    }

    @DeleteMapping(value = "/{ids}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ApiResponse<?> deleteCourse(@PathVariable List<Long> ids) {
        courseService.delete(ids);
        return ApiResponse.builder()
                .code(200)
                .message("Success")
                .build();
    }

    @GetMapping(value = "/list/{ids}")
    public ApiResponse<?> getListCourseById(@PathVariable List<Long> ids) {
        return ApiResponse.builder()
                .code(200)
                .data(courseService.getListCourseById(ids))
                .message("Success")
                .build();
    }

    @GetMapping(value = "/{id}")
    public ApiResponse<CourseResponse> getCourseById(@PathVariable Long id) {
        return ApiResponse.<CourseResponse>builder()
                .code(200)
                .message("Success")
                .data(courseService.getCourseById(id))
                .build();
    }

    @GetMapping(value = "/search-advance")
    public ApiResponse<PageResponse<?>> searchAdvanceCourse(@RequestParam Map<String, Object> filters,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size,
                                                            @RequestParam(defaultValue = "id,desc") String sort) {

        Map<String, Object> searchFilters = new HashMap<>(filters);
        Arrays.asList("page", "size", "sort").forEach(searchFilters.keySet()::remove);

        return ApiResponse.<PageResponse<?>>builder()
                .data(courseService.searchAdvanceCourse(searchFilters,page,size,sort))
                .code(200)
                .message("Search course success!")
                .build();
    }

}
