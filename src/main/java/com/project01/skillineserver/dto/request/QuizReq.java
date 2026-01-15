package com.project01.skillineserver.dto.request;

import java.time.Instant;

public record QuizReq(Long id, String lectureId, String title, String desc,
                      Integer position, Instant timeLimit, Integer maxAttempt) {
}
