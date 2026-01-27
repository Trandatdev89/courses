package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.dto.reponse.AnswerUserRes;
import com.project01.skillineserver.dto.reponse.HistoryExamUser;
import com.project01.skillineserver.dto.reponse.QuestionExamUser;
import com.project01.skillineserver.entity.AnswerEntity;
import com.project01.skillineserver.entity.QuestionEntity;
import com.project01.skillineserver.entity.QuizAttemptEntity;
import com.project01.skillineserver.projection.AnswerUserChoiceProjection;
import com.project01.skillineserver.projection.HistoryExamFlatProjection;
import com.project01.skillineserver.repository.AnswerRepository;
import com.project01.skillineserver.repository.HistoryScoreUserRepository;
import com.project01.skillineserver.repository.QuestionRepository;
import com.project01.skillineserver.repository.QuizAttemptRepository;
import com.project01.skillineserver.service.HistoryScoreUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryScoreUserServiceImpl implements HistoryScoreUserService {

    private final HistoryScoreUserRepository historyScoreUserRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    public HistoryExamUser getHistoryScoreExamOfUser(Long attemptQuizId) {
        QuizAttemptEntity quizAttemptOfUser = quizAttemptRepository.findQuizAttemptOfUserById(attemptQuizId);

        //List Question:
        List<QuestionEntity> questions = questionRepository.findAllByQuizId(quizAttemptOfUser.getQuizId());
        Set<Long> questionIds = questions.stream().map(QuestionEntity::getId).collect(Collectors.toSet());

        //List answers of list questionId
        Map<Long,List<AnswerEntity>> answerGroupByQuestionId  =  answerRepository
                .findAllByQuestionIdIn(questionIds)
                .stream()
                .collect(Collectors.groupingBy(AnswerEntity::getQuestionId));

        //List HistoryUserChoice:
        Map<Long,List<AnswerUserChoiceProjection>> answerUserChoiceGroupByQuestionId  = historyScoreUserRepository
                .findByAttemptQuizIdAndQuestionIdIn(attemptQuizId,questionIds)
                .stream()
                .collect(Collectors.groupingBy(AnswerUserChoiceProjection::getQuestionId));

        List<QuestionExamUser> questionExamUsers = questions.stream()
                .map(question -> buildQuestionExamUser(
                        question,
                        answerGroupByQuestionId.getOrDefault(question.getId(), Collections.emptyList()),
                        answerUserChoiceGroupByQuestionId.getOrDefault(question.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());


        return HistoryExamUser.builder()
                .submittedAt(quizAttemptOfUser.getSubmittedAt())
                .totalScore(quizAttemptOfUser.getTotalScore())
                .quizAttemptId(quizAttemptOfUser.getId())
                .questions(questionExamUsers)
                .build();
    }

    private QuestionExamUser buildQuestionExamUser(
            QuestionEntity question,
            List<AnswerEntity> answers,
            List<AnswerUserChoiceProjection> userChoices) {

        double scoreAchieved = userChoices.stream()
                .mapToDouble(choice -> choice.getScore() == null ? 0.0 : choice.getScore())
                .sum();

        // Set các answer user đã chọn
        Set<Long> selectedAnswerIds = userChoices.stream()
                .map(AnswerUserChoiceProjection::getAnswerUserChoice)
                .collect(Collectors.toSet());

        // Build answer responses
        List<AnswerUserRes> answerUserRes = answers.stream()
                .map(answer -> AnswerUserRes.builder()
                        .answerId(answer.getId())
                        .isCorrect(answer.isCorrect())
                        .isUserSelected(selectedAnswerIds.contains(answer.getId()))
                        .content(answer.getContent())
                        .build())
                .collect(Collectors.toList());

        return QuestionExamUser.builder()
                .questionId(question.getId())
                .maxScore(question.getScore())
                .type(question.getType())
                .content(question.getContent())
                .scoreAchieved(scoreAchieved)
                .answers(answerUserRes)
                .build();
    }

//    @Override
//    public HistoryExamUser getHistoryScoreExamOfUser(Long attemptQuizId) {
//
//        List<HistoryExamFlatProjection> rows =
//                quizAttemptRepository.getA(attemptQuizId);
//
//        if (rows.isEmpty()) {
//            return null; // hoặc throw exception
//        }
//
//        Map<Long, QuestionExamUser> questionMap = new LinkedHashMap<>();
//
//        for (HistoryExamFlatProjection row : rows) {
//
//            QuestionExamUser question = questionMap.computeIfAbsent(
//                    row.getQuestionId(),
//                    id -> QuestionExamUser.builder()
//                            .questionId(id)
//                            .content(row.getQuestionContent())
//                            .type(row.getQuestionType())
//                            .maxScore(row.getMaxScore())
//                            .scoreAchieved(row.getScoreAchieved())
//                            .answers(new ArrayList<>())
//                            .build()
//            );
//
//            // tránh duplicate answer (phòng hờ)
//            boolean exists = question.getAnswers().stream()
//                    .anyMatch(a -> a.getAnswerId().equals(row.getAnswerId()));
//
//            if (!exists) {
//                question.getAnswers().add(
//                        AnswerUserRes.builder()
//                                .answerId(row.getAnswerId())
//                                .content(row.getAnswerContent())
//                                .isCorrect(row.getIsCorrect())
//                                .isUserSelected(row.getIsUserSelected() != null && row.getIsUserSelected() == 1L)
//                                .build()
//                );
//            }
//        }
//
//        HistoryExamFlatProjection first = rows.get(0);
//
//        log.info("Data first {}",first.getTotalScore(),first.getSubmittedAt());
//
//        return HistoryExamUser.builder()
//                .quizAttemptId(first.getAttemptId())
//                .submittedAt(first.getSubmittedAt())
//                .totalScore(first.getTotalScore())
//                .questions(new ArrayList<>(questionMap.values()))
//                .build();
//    }

}
