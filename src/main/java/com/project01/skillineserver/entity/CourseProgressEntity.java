package com.project01.skillineserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "course_progress")
public class CourseProgressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "enrolment_id")
    private Long enrolmentId;
    @Column(name = "lecture_id")
    private String lectureId;
    @Column(name = "is_completed")
    private boolean isCompleted;
    @Column(name = "completed_at")
    private Instant completedAt;
}
