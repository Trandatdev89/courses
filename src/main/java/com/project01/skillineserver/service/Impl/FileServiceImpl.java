package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.entity.LectureEntity;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.enums.ProcessStatus;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.repository.LectureRepository;
import com.project01.skillineserver.service.FileService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${upload.directory.hls}")
    private String HLS_DIR;

    private final LectureRepository lectureRepository;

    @PostConstruct
    public void init() {
        File HLS_DIR_FOLDER = new File(HLS_DIR);
        if (!HLS_DIR_FOLDER.exists()) {
            HLS_DIR_FOLDER.mkdir();
            log.info("HLS_DIR FOLDER CREATED");
        }
    }

    @Async("videoProcessingExecutor")
    public void processVideoAsync(String videoId){
        try{
            processVideo(videoId);
            CompletableFuture.completedFuture(null);
        }catch (Exception e){
            updateLectureStatus(videoId, ProcessStatus.FAILED);
            CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public ResponseEntity<Resource> streamMasterPlaylist(String videoId) {
        return null;
    }

    @Override
    public ResponseEntity<Resource> streamSegment(String videoId, String segmentName) {
        return null;
    }

    @Override
    public void processVideo(String videoId) throws IOException, InterruptedException {
        LectureEntity lectureEntityInDB = lectureRepository.findById(videoId)
                .orElseThrow(() -> new AppException(ErrorCode.LECTURE_NOT_FOUND));

        //update status Processing:
        lectureEntityInDB.setProcessStatus(ProcessStatus.PROCESSING);
        lectureRepository.save(lectureEntityInDB);

        String filePathOfLecture = lectureEntityInDB.getFilePath();
        Path pathLectureInDB = Paths.get(filePathOfLecture);
        Path streamVideoHls = Paths.get(HLS_DIR,  videoId);
        Path outputPath = Files.createDirectories(streamVideoHls);

        String ffmpegCmd = String.format(
                "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\"  \"%s/master.m3u8\" ",
                pathLectureInDB, outputPath, outputPath
        );

        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", ffmpegCmd);
        processBuilder.inheritIO();

        Process process = processBuilder.start();
        int exit = process.waitFor();

        if (exit != 0) {
            lectureEntityInDB.setProcessStatus(ProcessStatus.FAILED);
            throw new AppException(ErrorCode.VIDEO_CAN_NOT_UPLOAD);
        }

        lectureEntityInDB.setProcessStatus(ProcessStatus.COMPLETED);
        lectureRepository.save(lectureEntityInDB);
    }

    private void updateLectureStatus(String videoId,ProcessStatus processStatus){
        LectureEntity lecture = lectureRepository.findById(videoId)
                .orElseThrow(() -> new AppException(ErrorCode.LECTURE_NOT_FOUND));
        lecture.setProcessStatus(processStatus);
        lectureRepository.save(lecture);
    }
}
