package com.project01.skillineserver.projection;

import java.util.List;

public interface QuestionProjection {
    String getContentQuestion();
    List<AnswerProjection> getAnswers();
    String getTypeQuestion();
    Double getScore();
}
