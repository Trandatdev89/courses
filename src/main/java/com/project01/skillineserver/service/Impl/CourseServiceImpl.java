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

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseMapper courseMapper;
    private final UploadUtil uploadUtil;

    @Override
    public List<CourseEntity> getAllByCategoryId(Long categoryId) {
        List<CourseEntity> courses = courseRepository.findAllByCategoryId(categoryId);
        if (courses.isEmpty()) {
            throw new AppException(ErrorCode.COURSE_EMPTY);
        }
        return courses;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = {AppException.class})
    public CourseEntity save(CourseReq courseReq) throws IOException {
        CourseEntity courseEntityInDB;

        if (courseReq.id() != null) {
            courseEntityInDB = courseRepository.findById(courseReq.id())
                    .orElseGet(CourseEntity::new);
        } else {
            courseEntityInDB = new CourseEntity();
        }

        courseEntityInDB.setCategoryId(courseReq.categoryId());
        courseEntityInDB.setDescription(courseReq.desc());
        courseEntityInDB.setStatus(true);
        courseEntityInDB.setPrice(courseReq.price());
        courseEntityInDB.setLevel(courseReq.level());
        courseEntityInDB.setDiscountPrice(courseReq.discount());
        courseEntityInDB.setTitle(courseReq.title());

        Path pathFile = null;
        if(courseReq.thumbnail()!=null && courseReq.thumbnail() instanceof MultipartFile){
            pathFile = uploadUtil.createPathFile(courseReq.thumbnail(), FileType.IMAGE);
        }

        courseEntityInDB.setThumbnail_url(pathFile.toString());

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
    public CourseEntity getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
    }

    @Override
    public void purchaseCourse(List<Long> idCourse, UserEntity user) {
        List<EnrollmentEntity> enrollmentEntities = new ArrayList<>();
        for (Long courseId : idCourse) {
            EnrollmentEntity enrollmentEntity = EnrollmentEntity.builder()
                    .userId(user.getId())
                    .courseId(courseId)
                    .enrolledAt(Instant.now())
                    .progress(0l)
                    .build();
            enrollmentEntities.add(enrollmentEntity);
        }
        enrollmentRepository.saveAll(enrollmentEntities);
    }

    @Override
    public List<CourseEntity> getListCourseById(List<Long> ids) {
        return courseRepository.findAllByIdIn(ids);
    }

    @Override
    public List<CourseEntity> getCourseNotPagination() {
        return courseRepository.findAll();
    }

    @Override
    public PageResponse<CourseResponse> getCourses(int page, int size, String sort, String keyword) {
        Sort sortField =  Sort.by(Sort.Direction.DESC,"createAt");
        if(sort!=null && keyword!=null){
            sortField = SortField.ASC.getValue().equalsIgnoreCase(sort) ? Sort.by(Sort.Direction.ASC,keyword) : Sort.by(Sort.Direction.DESC,keyword);
        }
        PageRequest pageRequest  = PageRequest.of(page-1, size,sortField);

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
}
