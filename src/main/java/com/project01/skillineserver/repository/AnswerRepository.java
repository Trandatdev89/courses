package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AnswerRepository extends JpaRepository<AnswerEntity,Long> {
    List<AnswerEntity> findAllByQuestionId(Long questionId);
    List<AnswerEntity> findAllByQuestionIdIn(Set<Long> questionId);
}
