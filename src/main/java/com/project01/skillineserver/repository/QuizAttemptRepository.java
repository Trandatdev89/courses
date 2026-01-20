package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.AnswerEntity;
import com.project01.skillineserver.entity.QuizAttemptEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttemptEntity, Long> {
    QuizAttemptEntity findByQuizId(Long quizId);

    @Query("select qa.attemptNo as attemptNo " +
            "from QuizAttemptEntity qa " +
            "where qa.userId = ?1 and qa.quizId = ?2 " +
            "order by qa.attemptNo desc limit 1")
    Integer getAttemptNoOfUser(Long userId, Long quizId);

    @Query("select an from AnswerEntity an " +
            "inner join QuestionEntity qe on qe.id=an.questionId " +
            "inner join QuizEntity qu on qu.id=qe.quizId " +
            "where qu.id = ?1 and an.isCorrect = true")
    List<AnswerEntity> getAnswersByQuestionId(Long quizId);

    @Query("select qa.id from QuizAttemptEntity qa " +
            "where ?1 > qa.submittedAt " +
            "order by qa.submittedAt asc limit ?2")
    List<Long> findOldAttemptIds(Instant cutoffDate,Long batchSize);

    int deleteByIdIn(List<Long> quizAttemptIds);

    @Query("select qa.quizId," +
            "qa.userId," +
            "qa.attemptNo," +
            "qa.submittedAt," +
            "qa.totalScore " +
            "from QuizAttemptEntity qa " +
            "inner join QuizEntity qu on qu.id = qa.quizId " +
            "inner join UserEntity us on us.id = qa.userId " +
            "where qa.userId=?1 and qu.title like lower(concat('%',?2,'%'))" +
            "order by qa.totalScore desc ")
    Page<QuizAttemptEntity> getPageQuizAttemptOfUser(Long userId, String keyword, PageRequest pageRequest);
}
