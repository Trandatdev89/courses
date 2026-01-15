package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.reponse.ResultExamResponse;
import com.project01.skillineserver.dto.request.AttemptQuizReq;

public interface QuizAttemptService {
    ResultExamResponse save(AttemptQuizReq attemptQuizReq, Long userId);
}
