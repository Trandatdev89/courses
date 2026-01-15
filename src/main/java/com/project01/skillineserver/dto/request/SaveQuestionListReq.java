package com.project01.skillineserver.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaveQuestionListReq {
    private Long quizId;
    private List<QuestionReq> questions;
}
