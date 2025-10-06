package com.project01.skillineserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SortField {
    ASC("asc"),
    DESC("desc");

    private String value;

}
