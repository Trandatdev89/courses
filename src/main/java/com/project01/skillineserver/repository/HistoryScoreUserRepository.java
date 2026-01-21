package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.HistoryScoreUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryScoreUserRepository extends JpaRepository<HistoryScoreUserEntity,Long> {

    @Modifying
    @Query("delete from HistoryScoreUserEntity hsu " +
            "where hsu.attemptQuizId in :attemptQuizIds")
    int deleteByAttemptQuizIdIn(List<Long> attemptQuizIds);

    @Query("")
    Object getHistoryScoreExamOfUser(Long userId, Long quizId);
}
