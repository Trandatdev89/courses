package com.project01.skillineserver.projection;

public interface CourseProjection {
    Long getId();
    String getTitle();
    String getCategoryName();
    String getDescription();
    String getThumbnailUrl();
    String getLevel();
    Double getPrice();
    Double getRate();
}
