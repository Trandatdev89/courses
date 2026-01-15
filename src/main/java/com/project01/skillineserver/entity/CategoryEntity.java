package com.project01.skillineserver.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "category")
public class CategoryEntity extends BaseEntity<Long> {
    private String name;
    private String path;
    private boolean isActive;
}
