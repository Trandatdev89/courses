package com.project01.skillineserver.dto.request;

public record AnswerUserReq(Long questionId,Long answerId,String answerText,Double score){
}
