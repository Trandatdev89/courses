package com.project01.skillineserver.specification;

import com.project01.skillineserver.entity.CategoryEntity;
import com.project01.skillineserver.entity.CourseEntity;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CourseSpecifications {

    public Specification<CourseEntity> hasTitle(String title){
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("title"),
                    "%" + title + "%"
            );
        };
    }

    public Specification<CourseEntity> hasLevel(String level){
        return (root, query, criteriaBuilder) -> {
            if (level == null || level.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("level"),level);
        };
    }

    public  Specification<CourseEntity> hasCategoryId(Long categoryId){
        return (root, query, criteriaBuilder) -> {
            if(categoryId==null){
                return null;
            }

            Root<CategoryEntity> categoryEntityRoot = query.from(CategoryEntity.class);

            //PREDICATE:
            Predicate joinCondition = criteriaBuilder.equal(root.get("categoryId"),categoryEntityRoot.get("id"));

            Predicate nameCondition = criteriaBuilder.equal(categoryEntityRoot.get("id"),categoryId);

            return criteriaBuilder.and(joinCondition,nameCondition);
        };
    };

    public Specification<CourseEntity> hasField(String key,Object value){
        
        return (root, query, criteriaBuilder) -> {
            if(value==null || value.toString().trim().isEmpty()){
                return null;
            }
            if(isNumber(value)){
                return criteriaBuilder.equal(root.get(key),value);
            }else{
                return criteriaBuilder.like(root.get(key),"%"+value+"%");
            }
        };
    }

    public Specification<CourseEntity> hasFieldRange(String key,Object value){
        return (root, query, criteriaBuilder) -> {

            if (value == null || value.toString().trim().isEmpty()) {
                return null;
            }

            String val = value.toString();
            String fieldName;

            if (key.endsWith("Start")) {
                fieldName = key.replace("Start", "");
                return criteriaBuilder.greaterThanOrEqualTo(
                        root.get(fieldName),
                        Long.parseLong(val)
                );
            }

            if (key.endsWith("End")) {
                fieldName = key.replace("End", "");
                return criteriaBuilder.lessThanOrEqualTo(
                        root.get(fieldName),
                        Long.parseLong(val)
                );
            }

            return null;
        };
    }

    private boolean isNumber(Object value){
        String str = value.toString();
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
