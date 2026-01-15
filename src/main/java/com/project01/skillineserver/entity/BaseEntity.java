package com.project01.skillineserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<T extends Serializable> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private T id;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    public void generateId(){
        if(id==null&& isStringId()){
            id = (T) UUID.randomUUID().toString();
        }
    }

    private boolean isStringId(){
        Type genericSupperClass = getClass().getGenericSuperclass();
        if(genericSupperClass instanceof ParameterizedType){
            Type[] types = ((ParameterizedType)genericSupperClass).getActualTypeArguments();
            return types[0].equals(String.class);
        }

        return false;
    }
}
