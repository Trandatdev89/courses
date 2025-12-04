package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.CourseProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseProgressRepository extends JpaRepository<CourseProgressEntity,Long> {
}
