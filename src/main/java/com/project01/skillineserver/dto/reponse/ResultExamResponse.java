package com.project01.skillineserver.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ResultExamResponse {
    private Long quizId;
    private Double totalScore;
    private List<AnswerRes> answerRes;
}
