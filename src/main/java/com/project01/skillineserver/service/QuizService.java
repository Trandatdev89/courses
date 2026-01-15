package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.request.QuizReq;

import java.util.List;

public interface QuizService {
    void save(QuizReq quizReq);
    void delete(List<Long> quizIds);
}
