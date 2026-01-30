package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.dto.reponse.AnswerUserRes;
import com.project01.skillineserver.dto.reponse.QuestionExamUser;
import com.project01.skillineserver.dto.request.AnswerReq;
import com.project01.skillineserver.dto.request.QuestionReq;
import com.project01.skillineserver.dto.request.SaveQuestionListReq;
import com.project01.skillineserver.entity.AnswerEntity;
import com.project01.skillineserver.entity.QuestionEntity;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.projection.QuestionExamProjection;
import com.project01.skillineserver.repository.AnswerRepository;
import com.project01.skillineserver.repository.QuestionRepository;
import com.project01.skillineserver.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    @Transactional
    public void save(SaveQuestionListReq saveQuestionListReq) {
        List<QuestionEntity> questionsNeedSave = new ArrayList<>();
        List<AnswerEntity> answerNeedSave = new ArrayList<>();

        if (saveQuestionListReq.getQuestions() == null || saveQuestionListReq.getQuestions().isEmpty()) {
            throw new AppException(ErrorCode.QUESTION_EMPTY);
        }

        Set<Long> questionIds = saveQuestionListReq
                .getQuestions()
                .stream()
                .map(QuestionReq::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Long> newAnswerIds = saveQuestionListReq.getQuestions()
                .stream()
                .flatMap(q -> q.answerReqs().stream())
                .map(AnswerReq::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long,QuestionEntity> mapQuestion = questionIds.isEmpty()
                ? Collections.emptyMap()
                : questionRepository.findAllByIdIn(questionIds)
                .stream()
                .collect(Collectors.toMap(QuestionEntity::getId,questionEntity -> questionEntity));

        Map<Long,AnswerEntity> mapAnswer = newAnswerIds.isEmpty()
                ? Collections.emptyMap()
                : answerRepository.findAllByIdIn(newAnswerIds)
                .stream()
                .collect(Collectors.toMap(AnswerEntity::getId,answer -> answer));

        if(mapQuestion.size()!=questionIds.size()){
            throw new AppException(ErrorCode.QUESTION_NOT_EXITS);
        }

        for (QuestionReq questionReq : saveQuestionListReq.getQuestions()) {
            QuestionEntity question = questionReq.id()!=null ? mapQuestion.get(questionReq.id())
                    : new QuestionEntity();

            question.setQuizId(saveQuestionListReq.getQuizId());
            question.setScore(questionReq.score());
            question.setType(questionReq.type());
            question.setContent(questionReq.content());
            question.setPosition(questionReq.position());

            questionsNeedSave.add(question);
        }

        questionRepository.saveAll(questionsNeedSave);

        for (int i = 0; i < saveQuestionListReq.getQuestions().size(); i++) {
            QuestionEntity question = questionsNeedSave.get(i);
            QuestionReq questionReq = saveQuestionListReq.getQuestions().get(i);

            for (AnswerReq answerReq : questionReq.answerReqs()) {
                AnswerEntity answer =  answerReq.id()!=null ? mapAnswer.get(answerReq.id()) : new AnswerEntity();
                answer.setQuestionId(question.getId());
                answer.setContent(answerReq.content());
                answer.setCorrect(answerReq.isCorrect());
                answerNeedSave.add(answer);
            }
        }
        answerRepository.saveAll(answerNeedSave);
    }

    @Override
    public List<QuestionExamUser> exam(Long quizId) {

        List<QuestionExamProjection> rows =
                questionRepository.findQuestionByQuizId(quizId);

        if (rows.isEmpty()) {
            throw new AppException(ErrorCode.QUESTION_EMPTY);
        }

        Map<Long, QuestionExamUser> questionMap = new LinkedHashMap<>();

        for (QuestionExamProjection row : rows) {

            // Nếu question chưa tồn tại → tạo mới
            QuestionExamUser question = questionMap.computeIfAbsent(
                    row.getQuestionId(),
                    id -> QuestionExamUser.builder()
                            .questionId(row.getQuestionId())
                            .content(row.getQuestionContent())
                            .type(row.getType())
                            .maxScore(row.getScore())
                            .answers(new ArrayList<>())
                            .build()
            );

            // Add answer vào question
            question.getAnswers().add(
                    AnswerUserRes.builder()
                            .answerId(row.getAnswerId())
                            .content(row.getAnswerContent())
                            .build()
            );
        }

        return new ArrayList<>(questionMap.values());
    }


}
