package com.project01.skillineserver.projection;

public interface CourseProgressProjection {
    Long progressPercent();
    boolean isCompleted();
    String completeDate();
}
