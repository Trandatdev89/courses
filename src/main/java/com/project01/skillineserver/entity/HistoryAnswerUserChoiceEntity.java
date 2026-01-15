package com.project01.skillineserver.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "history_answer_user_choice")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryAnswerUserChoiceEntity {
    @EmbeddedId
    private QuizAttemptAnswerOptionId id;
}
