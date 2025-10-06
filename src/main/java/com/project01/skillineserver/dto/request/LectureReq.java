package com.project01.skillineserver.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record LectureReq(String id, String title, Long courseId,
                         Integer position, MultipartFile videoFile) {
}
