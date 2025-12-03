package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.reponse.CourseResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.CourseReq;
import com.project01.skillineserver.entity.CourseEntity;
import com.project01.skillineserver.entity.UserEntity;

import java.io.IOException;
import java.util.List;

public interface CourseService {
    List<CourseResponse> getAllByCategoryId(Long categoryId);
    CourseEntity save(CourseReq courseReq) throws IOException;
    void delete(List<Long> courseId);
    CourseResponse getCourseById(Long id);
    void purchaseCourse(List<Long> idCourse,Long userId);
    List<CourseResponse> getListCourseById(List<Long> ids);
    List<CourseResponse> getCourseNotPagination();
    PageResponse<CourseResponse> getCourses(int page, int size, String sort, String keyword);
}
