package com.project01.skillineserver.controller;


import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.request.SaveQuestionListReq;
import com.project01.skillineserver.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ApiResponse<?> save(@RequestBody SaveQuestionListReq saveQuestionListReq) {
        questionService.save(saveQuestionListReq);
        return ApiResponse.builder()
                .code(200)
                .message("Save question success!")
                .build();
    }

}
