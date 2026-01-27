package com.project01.skillineserver.projection;

public interface AnswerProjection {
    Long getAnswerId();
    String getAnswerContent();
    boolean isCorrect();
}
