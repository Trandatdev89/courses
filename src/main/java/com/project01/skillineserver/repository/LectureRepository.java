package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<LectureEntity,String> {
    List<LectureEntity> findAllByCourseId(Long courseId);
}
