//package com.project01.skillineserver.mapper;
//
//import com.project01.skillineserver.dto.reponse.QuestionExamUser;
//import com.project01.skillineserver.projection.QuestionExamProjection;
//import org.springframework.stereotype.Component;
//
//@Component
//public class QuestionMapper {
//
//    public QuestionExamUser toQuestionExamUser(QuestionExamProjection questionExamProjection){
//        return QuestionExamUser.builder()
//                .questionId(questionExamProjection.getQuestionId())
//                .type(questionExamProjection.getType())
//                .content(questionExamProjection.getQuestionContent())
//                .maxScore(questionExamProjection.getScore())
//                .answers()
//                .build();
//    }
//}
