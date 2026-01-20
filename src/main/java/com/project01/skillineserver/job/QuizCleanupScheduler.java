//package com.project01.skillineserver.job;
//
//import com.project01.skillineserver.repository.HistoryAnswerUserChoiceRepository;
//import com.project01.skillineserver.repository.HistoryScoreUserRepository;
//import com.project01.skillineserver.repository.QuizAttemptRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class QuizCleanupScheduler {
//
//    private final QuizAttemptRepository quizAttemptRepository;
//    private final HistoryAnswerUserChoiceRepository historyAnswerUserChoiceRepository;
//    private final HistoryScoreUserRepository historyScoreUserRepository;
//
//    @Value("${scheduler.quiz-cleanup.retention-days}")
//    private Long retentionDay;
//
//    @Value("${scheduler.quiz-cleanup.batch-size}")
//    private Long batchSize;
//
//    @Scheduled(cron = "${scheduler.quiz-cleanup.cron}")
//    @Transactional
//    public void cleanupOldQuizAttempt(){
//        log.info("Starting cleanup of quiz attempts older than {} days",
//                retentionDay);
//
//        try{
//
//            Instant cutoffDate = Instant.now().minus(retentionDay, ChronoUnit.DAYS);
//            int totalDelete = 0;
//            int iteration = 0;
//
//            while(true){
//
//                iteration++;
//
//                List<Long> attemptIds = quizAttemptRepository.findOldAttemptIds(cutoffDate,batchSize);
//                if(attemptIds.isEmpty()){
//                    log.info("No more quiz attempts to cleanup");
//                    break;
//                }
//
//                log.info("Processing batch {} with {} attempts", iteration, attemptIds.size());
//
//                int deletedChoices = historyAnswerUserChoiceRepository
//                        .deleteByAttemptQuizIds(attemptIds);
//
//                int deletedHistory = historyScoreUserRepository
//                        .deleteByAttemptQuizIdIn(attemptIds);
//
//                int deletedAttempts = quizAttemptRepository
//                        .deleteByIdIn(attemptIds);
//
//                totalDelete += deletedAttempts;
//
//                log.info("Batch {}: Deleted {} choices, {} history, {} attempts",
//                        iteration, deletedChoices, deletedHistory, deletedAttempts);
//
//                Thread.sleep(100);
//            }
//
//        }catch(Exception e){
//            throw new RuntimeException("Quiz cleanup failed", e);
//        }
//
//    }
//}
