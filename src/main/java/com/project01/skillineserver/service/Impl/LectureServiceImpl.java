package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.constants.AppConstants;
import com.project01.skillineserver.dto.reponse.LectureResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.LectureReq;
import com.project01.skillineserver.entity.CourseEntity;
import com.project01.skillineserver.entity.LectureEntity;
import com.project01.skillineserver.entity.OrderEntity;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.enums.SortField;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.mapper.LectureMapper;
import com.project01.skillineserver.repository.LectureRepository;
import com.project01.skillineserver.service.FileService;
import com.project01.skillineserver.service.LectureService;
import com.project01.skillineserver.utils.UploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final UploadUtil uploadUtil;
    private final FileService fileService;
    private final LectureMapper lectureMapper;

    @Override
    public LectureEntity save(LectureReq lectureReq) throws IOException, InterruptedException {

        LectureEntity lectureEntity;

        if (lectureReq.id() != null) {
            lectureEntity =  lectureRepository.findById(lectureReq.id())
                    .orElseGet(LectureEntity::new);
        } else {
            lectureEntity = new LectureEntity();
        }


        if (lectureReq.videoFile() != null) {
            lectureEntity = uploadUtil.generateVideoUrl(lectureReq.videoFile(), lectureEntity);
        }

        lectureEntity.setTitle(lectureReq.title());
        lectureEntity.setPosition(lectureReq.position());
        lectureEntity.setCourseId(lectureReq.courseId());
        lectureEntity.setUpdateAt(Instant.now());
        lectureEntity.setCreateAt(Instant.now());

        LectureEntity lectureNeedSave = lectureRepository.save(lectureEntity);

        fileService.processVideo(lectureNeedSave.getId());

        return lectureNeedSave;
    }

    @Override
    public ResponseEntity<Resource> streamBasicVideo(String id) {
        LectureEntity lectureEntity = lectureRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LECTURE_NOT_FOUND));

        String contentType = lectureEntity.getContentType();
        String filePath = lectureEntity.getFilePath();
        Path path = Paths.get(filePath);

        Resource resource = new FileSystemResource(path);

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);

    }

    @Override
    public ResponseEntity<Resource> streamRangeLecture(String id, String range) {
        System.out.println(range);
        //

        LectureEntity video = lectureRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LECTURE_NOT_FOUND));
        Path path = Paths.get(video.getFilePath());

        Resource resource = new FileSystemResource(path);

        String contentType = video.getContentType();

        if (contentType == null) {
            contentType = "application/octet-stream";

        }

        //file ki length
        long fileLength = path.toFile().length();
        log.info("Length all file video :{}",fileLength);


        //pahle jaisa hi code hai kyuki range header null
        if (range == null) {
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
        }

        //calculating start and end range

        long rangeStart;

        long rangeEnd;

        String[] ranges = range.replace("bytes=", "").split("-");
        rangeStart = Long.parseLong(ranges[0]);

        rangeEnd = rangeStart + AppConstants.CHUNK_SIZE - 1;

        if (rangeEnd >= fileLength) {
            rangeEnd = fileLength - 1;

            log.info("RangeEnd is max limit :{}",rangeEnd);
        }

        log.info("range start : {}" , rangeStart);
        log.info("range end : {}" , rangeEnd);
        InputStream inputStream;

        try {

            inputStream = Files.newInputStream(path);
            inputStream.skip(rangeStart);
            long contentLength = rangeEnd - rangeStart + 1;

            byte[] data = new byte[(int) contentLength];
            int read = inputStream.read(data, 0, data.length);
            log.info("read(number of bytes) : {}", read);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("X-Content-Type-Options", "nosniff");
            headers.setContentLength(contentLength);

            return ResponseEntity
                    .status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new ByteArrayResource(data));


        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public PageResponse<LectureResponse> getListLecture(int page, int size, String sort, String keyword,Long courseId) {
        Sort sortField =  Sort.by(Sort.Direction.DESC,"createAt");
        if(sort!=null && keyword!=null){
            sortField = SortField.ASC.getValue().equalsIgnoreCase(sort) ? Sort.by(Sort.Direction.ASC,keyword) : Sort.by(Sort.Direction.DESC,keyword);
        }
        PageRequest pageRequest  = PageRequest.of(page-1, size,sortField);

        Page<LectureEntity> orders = lectureRepository.findAllByCourseId(pageRequest,courseId);

        List<LectureResponse> listLectureResponse = orders.getContent().stream().map(lectureMapper::toLectureResponse).toList();

        return PageResponse.<LectureResponse>builder()
                .list(listLectureResponse)
                .page(page)
                .size(size)
                .totalElements(orders.getTotalElements())
                .totalPages(orders.getTotalPages())
                .build();
    }

    @Override
    public List<LectureResponse> getListLectureNotPagi(Long courseId) {
        List<LectureEntity> lectureEntityList  = lectureRepository.findAllByCourseId(courseId);
        return lectureEntityList.stream().map(lectureMapper::toLectureResponse).toList();
    }

}
