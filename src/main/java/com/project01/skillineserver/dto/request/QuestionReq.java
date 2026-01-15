package com.project01.skillineserver.dto.request;

import com.project01.skillineserver.enums.QuestionType;

import java.util.List;

public record QuestionReq(Long id, String content, List<AnswerReq> answerReqs,
                          QuestionType type, Integer position, Double score){
}
