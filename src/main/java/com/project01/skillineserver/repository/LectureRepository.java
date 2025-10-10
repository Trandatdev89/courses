package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.LectureEntity;
import com.project01.skillineserver.projection.CourseProjection;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LectureRepository extends JpaRepository<LectureEntity,String> {
    Page<LectureEntity> findAllByCourseId(PageRequest pageRequest, Long courseId);

    @Query("SELECT co.title as title, " +
            "cat.name as categoryName " +
            "FROM CourseEntity co " +
            "LEFT JOIN CategoryEntity cat on co.categoryId=cat.id " +
            "WHERE co.id = :courseId")
    CourseProjection getCourseWithCategory(@Param("courseId") Long courseId);


    List<LectureEntity> findAllByCourseId(Long courseId);
}
