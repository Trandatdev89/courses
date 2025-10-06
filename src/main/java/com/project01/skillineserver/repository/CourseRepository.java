package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<CourseEntity,Long> {
    List<CourseEntity> findAllByCategoryId(Long categoryId);

    List<CourseEntity> findAllByIdIn(List<Long> id);
}
