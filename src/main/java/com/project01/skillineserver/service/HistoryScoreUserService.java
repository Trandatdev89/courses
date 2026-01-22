package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.reponse.HistoryExamUser;
import com.project01.skillineserver.dto.reponse.QuestionExamUser;


public interface HistoryScoreUserService {
    HistoryExamUser getHistoryScoreExamOfUser(Long attemptQuizId);
}
