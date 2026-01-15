package com.project01.skillineserver.dto.request;
import java.util.List;

public record AttemptQuizReq(Long quizId, List<AnswerUserReq> answerUserReqs) {
}
