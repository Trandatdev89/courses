package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.reponse.ResultExamResponse;
import com.project01.skillineserver.dto.request.AttemptQuizReq;
import com.project01.skillineserver.entity.QuizAttemptEntity;

import java.util.List;

public interface QuizAttemptService {
    ResultExamResponse save(AttemptQuizReq attemptQuizReq, Long userId);
    List<QuizAttemptEntity> getQuizAttempts(int page, int size, String sort, String keyword);
}
