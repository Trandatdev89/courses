package com.project01.skillineserver.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureResponse {
    private String id;
    private Integer position;
    private String title;
    private String urlThumbnail;
    private String courseName;
    private String CategoryName;
    private String duration;
    private String createAt;
    private String updateAt;
}
