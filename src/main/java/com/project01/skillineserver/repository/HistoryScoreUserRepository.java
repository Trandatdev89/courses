package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.HistoryScoreUserEntity;
import com.project01.skillineserver.projection.AnswerUserChoiceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface HistoryScoreUserRepository extends JpaRepository<HistoryScoreUserEntity, Long> {

    @Modifying
    @Query("delete from HistoryScoreUserEntity hsu " +
            "where hsu.attemptQuizId in :attemptQuizIds")
    int deleteByAttemptQuizIdIn(List<Long> attemptQuizIds);

    @Query("""
                select hsu.score,
                hauc.id.answerId as answerUserChoice,
                hsu.questionId as questionId
                from HistoryScoreUserEntity hsu
                inner join HistoryAnswerUserChoiceEntity hauc on hauc.id.historyAnswerUserId = hsu.id
                where hsu.attemptQuizId = ?1
                  and hsu.questionId in ?2
            """)
    List<AnswerUserChoiceProjection> findByAttemptQuizIdAndQuestionIdIn(Long attemptQuizId, Set<Long> questionId);

}
