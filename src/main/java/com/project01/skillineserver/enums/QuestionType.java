package com.project01.skillineserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuestionType {
    SINGLE("SINGLE"),
    MULTIPLE("MULTIPLE");
    private final String value;
}
