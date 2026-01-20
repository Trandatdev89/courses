package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.HistoryAnswerUserChoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryAnswerUserChoiceRepository extends JpaRepository<HistoryAnswerUserChoiceEntity,Long> {

//    @Modifying
//    @Query("delete from HistoryAnswerUserChoiceEntity hsa " +
//            "where hsa.id in (select hsu.id from HistoryScoreUserEntity hsu " +
//            "where hsu.id in :attemptIds) ")
//    int deleteByAttemptQuizIds(List<Long> attemptIds);
}
