package com.project01.skillineserver.controller;

import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.reponse.LectureResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.LectureReq;
import com.project01.skillineserver.entity.LectureEntity;
import com.project01.skillineserver.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/lecture")
public class LectureController {

    @Value("${upload.directory.hls}")
    private String HLS_DIR;

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

    @GetMapping(value = "/stream/{id}")
    @PreAuthorize("@authorizationService.isCanAccessApi()")
    public ResponseEntity<Resource> streamLecture(@PathVariable String id) throws IOException {
        return lectureService.streamBasicVideo(id);
    }

    @GetMapping(value = "/range/{id}")
    public ResponseEntity<Resource> streamRangeLecture(@PathVariable String id,
                                                       @RequestHeader(value = "Range", required = false) String range) {
        return lectureService.streamRangeLecture(id,range);
    }

    @GetMapping("/stream/{videoId}/master.m3u8")
    @PreAuthorize("@authorizationService.isCanAccessApi()")
    public ResponseEntity<Resource> streamMasterPlaylist(@PathVariable String videoId) {
        try {
            Path playlistPath = Paths.get(HLS_DIR, videoId, "master.m3u8");

            if (!Files.exists(playlistPath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(playlistPath.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.apple.mpegurl"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Stream video segments (.ts files)
    @GetMapping("/stream/{videoId}/{segmentName:.+\\.ts}")
    @PreAuthorize("@authorizationService.isCanAccessApi()")
    public ResponseEntity<Resource> streamSegment(
            @PathVariable String videoId,
            @PathVariable String segmentName) {
        try {
            Path segmentPath = Paths.get(HLS_DIR, videoId, segmentName);

            if (!Files.exists(segmentPath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(segmentPath.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("video/mp2t"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ApiResponse<PageResponse<LectureResponse>> listLecture(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(required = false) String sort,
                                                                @RequestParam(required = false) String keyword,
                                                                @RequestParam Long courseId) {
        return ApiResponse.<PageResponse<LectureResponse>>builder()
                .data(lectureService.getListLecture(page,size,sort,keyword,courseId))
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
