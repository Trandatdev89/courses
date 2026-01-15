package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.HistoryScoreUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryScoreUserRepository extends JpaRepository<HistoryScoreUserEntity,Long> {
}
