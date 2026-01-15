package com.project01.skillineserver.controller;


import com.project01.skillineserver.config.CustomUserDetail;
import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.reponse.ResultExamResponse;
import com.project01.skillineserver.dto.request.AttemptQuizReq;
import com.project01.skillineserver.service.QuizAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/attempt-quiz")
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    @PostMapping
    public ApiResponse<ResultExamResponse> save(@RequestBody AttemptQuizReq attemptQuizReq,
                               @AuthenticationPrincipal CustomUserDetail customUserDetail){
        return ApiResponse.<ResultExamResponse>builder()
                .data(quizAttemptService.save(attemptQuizReq,customUserDetail.getUser().getId()))
                .code(200)
                .message("Save exam quiz success!")
                .build();
    }
}
