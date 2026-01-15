package com.project01.skillineserver.controller;

import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.request.QuizReq;
import com.project01.skillineserver.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/quiz")
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    public ApiResponse<?> save(@RequestBody QuizReq quizReq){
        quizService.save(quizReq);
        return ApiResponse.builder()
                .code(200)
                .message("Save quiz success!")
                .build();
    }

    @DeleteMapping(value = "/{quizIds}")
    public ApiResponse<?> delete(@PathVariable List<Long> quizIds){
        quizService.delete(quizIds);
        return ApiResponse.builder()
                .code(200)
                .message("Delete quiz success!")
                .build();
    }

}
