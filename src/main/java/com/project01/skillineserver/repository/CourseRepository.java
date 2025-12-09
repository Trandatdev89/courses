package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<CourseEntity,Long>, JpaSpecificationExecutor<CourseEntity> {
    List<CourseEntity> findAllByCategoryId(Long categoryId);

    List<CourseEntity> findAllByIdIn(List<Long> id);

    @Modifying
    @Query("delete from CourseEntity c where c.categoryId in :categoryIds")
    void deleteAllByCategoryIdIn(List<Long> categoryIds);
}
