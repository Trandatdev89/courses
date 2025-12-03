package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.dto.reponse.CourseResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.CourseReq;
import com.project01.skillineserver.entity.*;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.enums.FileType;
import com.project01.skillineserver.enums.SortField;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.mapper.CourseMapper;
import com.project01.skillineserver.repository.CourseRepository;
import com.project01.skillineserver.repository.EnrollmentRepository;
import com.project01.skillineserver.repository.UserRepository;
import com.project01.skillineserver.service.CourseService;
import com.project01.skillineserver.utils.UploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseMapper courseMapper;
    private final UploadUtil uploadUtil;

    @Override
    public List<CourseResponse> getAllByCategoryId(Long categoryId) {
        List<CourseEntity> courses = courseRepository.findAllByCategoryId(categoryId);
        if (courses.isEmpty()) {
            throw new AppException(ErrorCode.COURSE_EMPTY);
        }

        List<CourseResponse> courseResponses = courses.stream().map(courseMapper::toLectureResponse).toList();
        return courseResponses;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {AppException.class})
    public CourseEntity save(CourseReq courseReq) throws IOException {

        CourseEntity courseEntityInDB = Optional.ofNullable(courseReq.id())
                .flatMap(courseRepository::findById)
                .orElse(new CourseEntity());

        String pathImage = resolvePathFile(courseReq.thumbnail(),courseEntityInDB.getThumbnail_url());

        courseEntityInDB.setCategoryId(courseReq.categoryId());
        courseEntityInDB.setDescription(courseReq.desc());
        courseEntityInDB.setStatus(true);
        courseEntityInDB.setPrice(courseReq.price());
        courseEntityInDB.setLevel(courseReq.level());
        courseEntityInDB.setDiscountPrice(courseReq.discount());
        courseEntityInDB.setTitle(courseReq.title());
        courseEntityInDB.setThumbnail_url(pathImage);

        return courseRepository.save(courseEntityInDB);
    }

    @Override
    public void delete(List<Long> courseId) {
        List<CourseEntity> courseEntityListInDB = courseRepository.findAllByIdIn(courseId);
        courseEntityListInDB.forEach(courseEntity -> {
            courseEntity.setStatus(false);
        });
        courseRepository.saveAll(courseEntityListInDB);
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        CourseEntity course = courseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        return courseMapper.toLectureResponse(course);
    }

    @Override
    public void purchaseCourse(List<Long> idCourse, Long userId) {
        List<EnrollmentEntity> enrollmentEntities = new ArrayList<>();
        for (Long courseId : idCourse) {
            EnrollmentEntity enrollmentEntity = EnrollmentEntity.builder()
                    .userId(userId)
                    .courseId(courseId)
                    .enrolledAt(Instant.now())
                    .progress(0l)
                    .build();
            enrollmentEntities.add(enrollmentEntity);
        }
        enrollmentRepository.saveAll(enrollmentEntities);
    }

    @Override
    public List<CourseResponse> getListCourseById(List<Long> ids) {
        return courseRepository.findAllByIdIn(ids).stream().map(courseMapper::toLectureResponse).toList();
    }

    @Override
    public List<CourseResponse> getCourseNotPagination() {
        return courseRepository.findAll().stream().map(courseMapper::toLectureResponse).toList();
    }

    @Override
    public PageResponse<CourseResponse> getCourses(int page, int size, String sort, String keyword) {
        Sort sortField = Sort.by(Sort.Direction.DESC, "createAt");
        if (sort != null && keyword != null) {
            sortField = SortField.ASC.getValue().equalsIgnoreCase(sort) ? Sort.by(Sort.Direction.ASC, keyword) : Sort.by(Sort.Direction.DESC, keyword);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size, sortField);

        Page<CourseEntity> orders = courseRepository.findAll(pageRequest);

        List<CourseResponse> courseResponseList = orders.getContent().stream().map(courseMapper::toLectureResponse).toList();

        return PageResponse.<CourseResponse>builder()
                .list(courseResponseList)
                .page(page)
                .size(size)
                .totalElements(orders.getTotalElements())
                .totalPages(orders.getTotalPages())
                .build();
    }

    private String resolvePathFile(Object inputFile, String pathFile) throws IOException {
        if (inputFile instanceof MultipartFile multipartFile) {
            return uploadUtil.createPathFile(multipartFile,FileType.IMAGE).toString();
        }else{
            return pathFile!=null ? pathFile : "";
        }
    }
}
