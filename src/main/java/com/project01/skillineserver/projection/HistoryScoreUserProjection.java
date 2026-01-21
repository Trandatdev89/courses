package com.project01.skillineserver.projection;

import java.time.Instant;
import java.util.List;

public interface HistoryScoreUserProjection {
     Long getQuizAttemptId();
     Instant getSubmitAt();
     Instant getTimeLimitQuiz();
     Double getTotalScore();
     List<QuestionProjection> getListQuestions();
}
