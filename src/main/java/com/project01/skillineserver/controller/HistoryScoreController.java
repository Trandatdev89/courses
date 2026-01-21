package com.project01.skillineserver.controller;


import com.project01.skillineserver.config.CustomUserDetail;
import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.service.HistoryScoreUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/history-score-user")
public class HistoryScoreController {

    private final HistoryScoreUserService historyScoreUserService;

    @GetMapping(value = "/exam")
    public ApiResponse<?> getHistoryScoreExamOfUser(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                    @RequestParam Long quizId){

        return ApiResponse.builder()
                .message("Get history exam user success!")
                .data(historyScoreUserService.getHistoryScoreExamOfUser(customUserDetail.getUser().getId(),quizId))
                .code(200)
                .build();
    }
}
