package com.project01.skillineserver.repository.custom;

import com.project01.skillineserver.dto.reponse.CourseResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.entity.CourseEntity;

import java.util.Map;

public interface CustomCourseRepository {
    PageResponse<CourseResponse> searchAdvanceCourse(Map<String, Object> filters, int page, int size, String sort);
}
