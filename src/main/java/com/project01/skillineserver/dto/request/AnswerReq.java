package com.project01.skillineserver.dto.request;

public record AnswerReq(Long id,String content,
                         boolean isCorrect) {
}
