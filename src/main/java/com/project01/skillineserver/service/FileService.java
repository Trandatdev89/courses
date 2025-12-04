package com.project01.skillineserver.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface FileService {
    void processVideo(String videoId) throws IOException, InterruptedException;
    void processVideoAsync(String videoId);

    ResponseEntity<Resource> streamMasterPlaylist(String videoId);
    ResponseEntity<Resource> streamSegment(String videoId,String segmentName);
}
