package com.project01.skillineserver.service;

import org.springframework.validation.ObjectError;

public interface HistoryScoreUserService {
    Object getHistoryScoreExamOfUser(Long userId,Long quizId);
}
