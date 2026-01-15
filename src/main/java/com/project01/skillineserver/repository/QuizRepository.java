package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<QuizEntity,Long> {
    void deleteByIdIn(List<Long> ids);
}
