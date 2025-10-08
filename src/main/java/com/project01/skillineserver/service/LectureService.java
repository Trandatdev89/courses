package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.reponse.LectureResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.LectureReq;
import com.project01.skillineserver.entity.LectureEntity;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface LectureService {
    LectureEntity save(LectureReq lectureReq) throws IOException, InterruptedException;
    ResponseEntity<Resource> streamBasicVideo(String id);
    ResponseEntity<Resource> streamRangeLecture(String id,String range);
    PageResponse<LectureResponse> getListLecture(int page, int size, String sort, String keyword, Long courseId);
}
