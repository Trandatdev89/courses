package com.project01.skillineserver.controller;

import com.project01.skillineserver.service.CourseProgressService;
import com.project01.skillineserver.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/file")
public class MediaController {

    @Value("${upload.directory.hls}")
    private String HLS_DIR;

    private final MediaService mediaService;


    @GetMapping(value = "/stream/{id}")
    public ResponseEntity<Resource> streamLecture(@PathVariable String id) throws IOException {
        return mediaService.streamBasicVideo(id);
    }

    @GetMapping(value = "/range/{id}")
    public ResponseEntity<Resource> streamRangeLecture(@PathVariable String id,
                                                       @RequestHeader(value = "Range", required = false) String range) {
        return mediaService.streamRangeLecture(id,range);
    }

    @GetMapping("/stream/{videoId}/master.m3u8")
    public ResponseEntity<Resource> streamMasterPlaylist(@PathVariable String videoId) {
        return mediaService.streamMasterPlaylist(videoId);
    }

    // Stream video segments (.ts files)
    @GetMapping("/stream/{videoId}/{segmentName:.+\\.ts}")
    public ResponseEntity<Resource> streamSegment(
            @PathVariable String videoId,
            @PathVariable String segmentName) {
        return mediaService.streamSegment(videoId,segmentName);
    }
}
