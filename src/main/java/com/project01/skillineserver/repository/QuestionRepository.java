package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.QuestionEntity;
import com.project01.skillineserver.entity.QuizAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionEntity,Long> {
    QuestionEntity findByQuizId(Long quizId);
    List<QuestionEntity> findAllByQuizId(Long quizId);
}
