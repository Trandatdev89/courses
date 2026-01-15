package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.dto.reponse.AnswerRes;
import com.project01.skillineserver.dto.reponse.ResultExamResponse;
import com.project01.skillineserver.dto.request.AnswerUserReq;
import com.project01.skillineserver.dto.request.AttemptQuizReq;
import com.project01.skillineserver.entity.*;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.mapper.QuizAttemptMapper;
import com.project01.skillineserver.repository.HistoryAnswerUserChoiceRepository;
import com.project01.skillineserver.repository.HistoryScoreUserRepository;
import com.project01.skillineserver.repository.QuizAttemptRepository;
import com.project01.skillineserver.service.QuizAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizAttemptServiceImpl implements QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizAttemptMapper quizAttemptMapper;
    private final HistoryScoreUserRepository historyScoreUserRepository;
    private final HistoryAnswerUserChoiceRepository historyAnswerUserChoiceRepository;

    @Override
    @Transactional
    public ResultExamResponse save(AttemptQuizReq attemptQuizReq, Long userId) {
        QuizAttemptEntity quizAttempt = new QuizAttemptEntity();
        Integer attemptNo = quizAttemptRepository.getAttemptNoOfUser(userId, attemptQuizReq.quizId());

        if(attemptNo>5){
            throw new AppException(ErrorCode.QUIZ_MAX_FIVE);
        }
        attemptNo = attemptNo == null ? 1 : Math.min(attemptNo + 1, 5); // thi toi da 5 lan
        quizAttempt.setAttemptNo(attemptNo);
        quizAttempt.setQuizId(attemptQuizReq.quizId());
        quizAttempt.setSubmittedAt(Instant.now());
        quizAttempt.setUserId(userId);
        quizAttempt.setTotalScore(0D);

        quizAttemptRepository.save(quizAttempt);

        Map<Long, Set<Long>> correctAnswerMap = new HashMap<>();
        List<AnswerEntity> correctAnswers = quizAttemptRepository.getAnswersByQuestionId(attemptQuizReq.quizId());
        for (AnswerEntity a : correctAnswers) {
            correctAnswerMap
                    .computeIfAbsent(a.getQuestionId(), k -> new HashSet<>())
                    .add(a.getId());
        }

        log.info("Load Answer example of teacher ,{}", correctAnswerMap.toString());
        double totalScore = 0;
        List<AnswerRes> answerRes = new ArrayList<>();
        List<HistoryScoreUserEntity> historyScoreUserNeedSave = new ArrayList<>();

        for (AnswerUserReq answerUserReq : attemptQuizReq.answerUserReqs()) {

            Set<Long> correctIds = correctAnswerMap.get(answerUserReq.questionId());
            boolean isCorrect = correctIds.contains(answerUserReq.answerId());
            double score = isCorrect ? answerUserReq.score() : 0D;
            totalScore += score;


            answerRes.add(AnswerRes.builder()
                    .isCorrect(isCorrect)
                    .questionId(answerUserReq.questionId())
                    .build());

            historyScoreUserNeedSave.add(HistoryScoreUserEntity.builder()
                    .questionId(answerUserReq.questionId())
                    .score(score)
                    .answerText(answerUserReq.answerText())
                    .attemptQuizId(quizAttempt.getId())
                    .build());
        }

        //save lich su thi
        quizAttempt.setTotalScore(totalScore);
        quizAttemptRepository.save(quizAttempt);
        historyScoreUserRepository.saveAll(historyScoreUserNeedSave);

        //save detail answer then user choice
        List<HistoryAnswerUserChoiceEntity> options = new ArrayList<>();
        Map<Long, List<Long>> questionHistoryMap =
                historyScoreUserNeedSave.stream()
                        .collect(Collectors.groupingBy(
                                HistoryScoreUserEntity::getQuestionId,
                                Collectors.mapping(
                                        HistoryScoreUserEntity::getId,
                                        Collectors.toList()
                                )
                        ));


        for (AnswerUserReq req : attemptQuizReq.answerUserReqs()) {
            List<Long> historyIds = questionHistoryMap.get(req.questionId());

            for (Long historyId : historyIds) {
                options.add(
                        new HistoryAnswerUserChoiceEntity(
                                new QuizAttemptAnswerOptionId(historyId, req.answerId())
                        )
                );
            }
        }
        historyAnswerUserChoiceRepository.saveAll(options);

        return quizAttemptMapper.toResultExamResponse(attemptQuizReq.quizId(), totalScore, answerRes);
    }
}
