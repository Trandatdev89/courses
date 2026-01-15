package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.dto.request.QuizReq;
import com.project01.skillineserver.entity.QuestionEntity;
import com.project01.skillineserver.entity.QuizAttemptEntity;
import com.project01.skillineserver.entity.QuizEntity;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.repository.QuestionRepository;
import com.project01.skillineserver.repository.QuizAttemptRepository;
import com.project01.skillineserver.repository.QuizRepository;
import com.project01.skillineserver.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuestionRepository questionRepository;

    @Override
    @Transactional
    public void save(QuizReq quizReq) {
        boolean isUpdate = quizReq.id()!=null;
        QuizEntity quizEntity;
        if(isUpdate){
            quizEntity = quizRepository.findById(quizReq.id()).orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_EXITS));
        }else{
            quizEntity = new QuizEntity();
        }

        quizEntity.setTitle(quizReq.title());
        quizEntity.setDescription(quizReq.desc());
        quizEntity.setLectureId(quizReq.lectureId());
        quizEntity.setTimeLimit(quizReq.timeLimit());
        quizEntity.setMaxAttempt(quizReq.maxAttempt());

        quizRepository.save(quizEntity);
    }

    @Override
    public void delete(List<Long> quizIds) {

        if(quizIds==null || quizIds.isEmpty()){
            throw new AppException(ErrorCode.QUIZ_ID_REQUIRE);
        }

        List<QuizAttemptEntity> listQuizAttemptNeedRemove  = new ArrayList<>();
        List<QuestionEntity> listQuestionNeedRemove  = new ArrayList<>();

        for (Long quizId : quizIds){
            listQuizAttemptNeedRemove.add(quizAttemptRepository.findByQuizId(quizId));
            listQuestionNeedRemove.add(questionRepository.findByQuizId(quizId));
        }

        quizAttemptRepository.deleteAll(listQuizAttemptNeedRemove);
        questionRepository.deleteAll(listQuestionNeedRemove);
        quizRepository.deleteByIdIn(quizIds);
    }
}
