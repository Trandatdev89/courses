package com.project01.skillineserver.dto.reponse;

import com.project01.skillineserver.enums.LevelEnum;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private Long id;
    private String title;
    private String categoryName;
    private LevelEnum level;
    private String thumbnail_url;
    private String description;
    private boolean status;
    private BigDecimal price;
    private String discount;
    private Double rate;
    private String createAt;
    private String updateAt;
    private Long categoryId;
}
