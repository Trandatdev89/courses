package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.config.CustomUserDetail;
import com.project01.skillineserver.dto.reponse.AnswerRes;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.reponse.ResultExamResponse;
import com.project01.skillineserver.dto.request.AnswerUserReq;
import com.project01.skillineserver.dto.request.AttemptQuizReq;
import com.project01.skillineserver.entity.*;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.mapper.QuizAttemptMapper;
import com.project01.skillineserver.projection.QuizAttemptProjection;
import com.project01.skillineserver.repository.HistoryAnswerUserChoiceRepository;
import com.project01.skillineserver.repository.HistoryScoreUserRepository;
import com.project01.skillineserver.repository.QuizAttemptRepository;
import com.project01.skillineserver.service.QuizAttemptService;
import com.project01.skillineserver.utils.AuthenticationUtil;
import com.project01.skillineserver.utils.MapUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;


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
        attemptNo = attemptNo == null ? 0 : attemptNo;
        if(attemptNo>5){
            throw new AppException(ErrorCode.QUIZ_MAX_FIVE);
        }
        attemptNo = Math.min(attemptNo + 1, 5); // thi toi da 5 lan

        Map<Long, Set<Long>> correctAnswerMap = new HashMap<>();
        List<AnswerEntity> correctAnswers = quizAttemptRepository.getAnswersByQuestionId(attemptQuizReq.quizId());
        for (AnswerEntity a : correctAnswers) {
            correctAnswerMap
                    .computeIfAbsent(a.getQuestionId(), k -> new HashSet<>())
                    .add(a.getId());
        }

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
                    .build());
        }

        quizAttempt.setAttemptNo(attemptNo);
        quizAttempt.setQuizId(attemptQuizReq.quizId());
        quizAttempt.setSubmittedAt(Instant.now());
        quizAttempt.setUserId(userId);
        quizAttempt.setTotalScore(totalScore);
        quizAttemptRepository.save(quizAttempt);

        //save lich su thi
        final Long attemptId = quizAttempt.getId();
        historyScoreUserNeedSave.forEach(h -> h.setAttemptQuizId(attemptId));
        historyScoreUserRepository.saveAll(historyScoreUserNeedSave);

        //save detail answer then user choice
        List<HistoryAnswerUserChoiceEntity> options = new ArrayList<>();
        for (int i = 0; i < attemptQuizReq.answerUserReqs().size(); i++) {
            AnswerUserReq req = attemptQuizReq.answerUserReqs().get(i);
            HistoryScoreUserEntity history = historyScoreUserNeedSave.get(i);

            options.add(new HistoryAnswerUserChoiceEntity(
                    new QuizAttemptAnswerOptionId(history.getId(), req.answerId())
            ));
        }

        historyAnswerUserChoiceRepository.saveAll(options);

        return quizAttemptMapper.toResultExamResponse(attemptQuizReq.quizId(), totalScore, answerRes);
    }

    @Override
    public PageResponse<QuizAttemptProjection> getQuizAttempts(int page, int size, String sort, String keyword) {
        Sort sortField = MapUtil.parseSort(sort);
        PageRequest pageRequest = PageRequest.of(page - 1, size, sortField);

        CustomUserDetail customUserDetail = AuthenticationUtil.getUserDetail();

        Page<QuizAttemptProjection> pageQuizAttempt = quizAttemptRepository
                .getPageQuizAttemptOfUser(customUserDetail.getUser().getId(),keyword,pageRequest);

        return PageResponse.<QuizAttemptProjection>builder()
                .totalPages(pageQuizAttempt.getTotalPages())
                .size(size)
                .page(page)
                .list(pageQuizAttempt.getContent())
                .totalElements(pageQuizAttempt.getTotalElements())
                .build();
    }
}
