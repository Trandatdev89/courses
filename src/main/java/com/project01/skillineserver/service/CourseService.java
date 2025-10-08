package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.CourseReq;
import com.project01.skillineserver.entity.CourseEntity;
import com.project01.skillineserver.entity.UserEntity;

import java.util.List;

public interface CourseService {
    List<CourseEntity> getAllByCategoryId(Long categoryId);
    CourseEntity save(CourseReq courseReq);
    void delete(List<Long> courseId);
    CourseEntity getCourseById(Long id);
    void purchaseCourse(List<Long> idCourse, UserEntity user);
    List<CourseEntity> getListCourseById(List<Long> ids);
    List<CourseEntity> getCourseNotPagination();
    PageResponse<CourseEntity> getCourses(int page, int size, String sort, String keyword);
}
