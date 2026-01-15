package com.project01.skillineserver.dto.reponse;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AnswerRes{
    private Long questionId;
    private boolean isCorrect = false;
//    private String content;
}
