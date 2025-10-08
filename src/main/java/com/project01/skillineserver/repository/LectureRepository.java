package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.LectureEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<LectureEntity,String> {
    Page<LectureEntity> findAllByCourseId(PageRequest pageRequest, Long courseId);
}
