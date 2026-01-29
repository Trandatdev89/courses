package com.project01.skillineserver.controller;

import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.reponse.LectureResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.LectureReq;
import com.project01.skillineserver.entity.LectureEntity;
import com.project01.skillineserver.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/lecture")
public class LectureController {

    private final LectureService lectureService;

    @PostMapping(value = "/save")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ApiResponse<LectureEntity> save(@ModelAttribute LectureReq lectureReq) throws IOException, InterruptedException {
        return ApiResponse.<LectureEntity>builder()
                .code(200)
                .message("Success")
                .data(lectureService.save(lectureReq))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<LectureResponse>> listLecture(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(required = false) String sort,
                                                                  @RequestParam(required = false) String keyword,
                                                                  @RequestParam Long courseId) {
        return ApiResponse.<PageResponse<LectureResponse>>builder()
                .data(lectureService.getListLecture(page, size, sort, keyword, courseId))
                .message("success!")
                .code(200)
                .build();
    }

    @GetMapping(value = "/not-pagi")
    public ApiResponse<List<LectureResponse>> listLecture(@RequestParam Long courseId) {
        return ApiResponse.<List<LectureResponse>>builder()
                .data(lectureService.getListLectureNotPagi(courseId))
                .message("success!")
                .code(200)
                .build();
    }
}
