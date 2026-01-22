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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryScoreUserServiceImpl implements HistoryScoreUserService {

    private final HistoryScoreUserRepository historyScoreUserRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

//    @Override
//    public HistoryExamUser getHistoryScoreExamOfUser(Long attemptQuizId) {
//
//        List<QuestionExamUser> questionExamUsers = new ArrayList<>();
//
//        QuizAttemptEntity attemptQuizOfUser = quizAttemptRepository.findQuizAttemptOfUserById(attemptQuizId);
//
//        List<QuestionEntity> questions = questionRepository.findAllByQuizId(attemptQuizOfUser.getQuizId());
//        for (QuestionEntity question : questions) {
//            List<AnswerUserRes> answerUserRes = new ArrayList<>();
//            List<AnswerEntity> answers = answerRepository.findAllByQuestionId(question.getId());
//            List<AnswerUserChoiceProjection> answerUserChoiceProjections = historyScoreUserRepository.findByAttemptQuizIdAndQuestionId(attemptQuizId,question.getId());
//
//            Set<Long> userAnswerIds = answerUserChoiceProjections
//                    .stream()
//                    .map(AnswerUserChoiceProjection::getAnswerUserChoice)
//                    .collect(Collectors.toSet());
//
//            Double scoreAchieved =
//                    answerUserChoiceProjections.stream()
//                            .mapToDouble(a -> a.getScore() == null ? 0.0 : a.getScore())
//                            .sum();
//
//            for(AnswerEntity answer : answers){
//                AnswerUserRes res = AnswerUserRes.builder()
//                        .isUserSelected(userAnswerIds.contains(answer.getId()))
//                        .content(answer.getContent())
//                        .isCorrect(answer.isCorrect())
//                        .answerId(answer.getId())
//                        .build();
//
//                answerUserRes.add(res);
//
//            }
//            QuestionExamUser questionExamUser = QuestionExamUser.builder()
//                    .questionId(question.getId())
//                    .type(question.getType())
//                    .maxScore(question.getScore())
//                    .content(question.getContent())
//                    .answers(answerUserRes)
//                    .scoreAchieved(scoreAchieved)
//                    .build();
//
//            questionExamUsers.add(questionExamUser);
//        }
//
//        return HistoryExamUser.builder()
//                .totalScore(attemptQuizOfUser.getTotalScore())
//                .questions(questionExamUsers)
//                .quizAttemptId(attemptQuizOfUser.getId())
//                .submittedAt(attemptQuizOfUser.getSubmittedAt())
//                .build();
//    }

    @Override
    public HistoryExamUser getHistoryScoreExamOfUser(Long attemptQuizId) {

        List<HistoryExamFlatProjection> rows =
                quizAttemptRepository.getA(attemptQuizId);

        if (rows.isEmpty()) {
            return null; // hoặc throw exception
        }

        Map<Long, QuestionExamUser> questionMap = new LinkedHashMap<>();

        for (HistoryExamFlatProjection row : rows) {

            QuestionExamUser question = questionMap.computeIfAbsent(
                    row.getQuestionId(),
                    id -> QuestionExamUser.builder()
                            .questionId(id)
                            .content(row.getQuestionContent())
                            .type(row.getQuestionType())
                            .maxScore(row.getMaxScore())
                            .scoreAchieved(row.getScoreAchieved())
                            .answers(new ArrayList<>())
                            .build()
            );

            // tránh duplicate answer (phòng hờ)
            boolean exists = question.getAnswers().stream()
                    .anyMatch(a -> a.getAnswerId().equals(row.getAnswerId()));

            if (!exists) {
                question.getAnswers().add(
                        AnswerUserRes.builder()
                                .answerId(row.getAnswerId())
                                .content(row.getAnswerContent())
                                .isCorrect(row.getIsCorrect())
                                .isUserSelected(row.getIsUserSelected() != null && row.getIsUserSelected() == 1L)
                                .build()
                );
            }
        }

        HistoryExamFlatProjection first = rows.get(0);

        return HistoryExamUser.builder()
                .quizAttemptId(first.getAttemptId())
                .submittedAt(first.getSubmittedAt())
                .totalScore(first.getTotalScore())
                .questions(new ArrayList<>(questionMap.values()))
                .build();
    }

}
