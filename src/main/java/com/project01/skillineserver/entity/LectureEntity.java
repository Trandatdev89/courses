package com.project01.skillineserver.entity;

import com.project01.skillineserver.enums.ProcessStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "lecture")
public class LectureEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    private String title;
    private String image;
    private Integer position;
    @Column(name = "course_id",nullable = false)
    private Long courseId;
    private String duration;
    @Column(name = "content_type",nullable = false)
    private String contentType;
    @Column(name = "file_path",nullable = false)
    private String filePath;

    @Enumerated(EnumType.STRING)
    private ProcessStatus processStatus;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createAt;

    @LastModifiedDate
    @Column(name = "update_at")
    private Instant updateAt;
}

