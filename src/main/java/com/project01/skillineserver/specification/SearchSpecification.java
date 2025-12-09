package com.project01.skillineserver.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

@Getter
@AllArgsConstructor
public class SearchSpecification<T> implements Specification<T> {

    private SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return switch (searchCriteria.getOperation()){
            case NEGATION -> criteriaBuilder.notEqual(root.get(searchCriteria.getKey()),searchCriteria.getValue());
            case GREATER_THAN -> criteriaBuilder.greaterThanOrEqualTo(root.get(searchCriteria.getKey()),searchCriteria.getValue().toString());
            case LESS_THAN -> criteriaBuilder.lessThanOrEqualTo(root.get(searchCriteria.getKey()),searchCriteria.getValue().toString());
            case LIKE -> criteriaBuilder.like(root.get(searchCriteria.getKey()),"%"+searchCriteria.getValue().toString()+"%");
            case CONTAINS -> criteriaBuilder.like(root.get(searchCriteria.getKey()),searchCriteria.getValue().toString());
            case EQUALITY -> criteriaBuilder.equal(root.get(searchCriteria.getKey()),searchCriteria.getValue());
            case STARTS_WITH -> criteriaBuilder.like(root.get(searchCriteria.getKey()),searchCriteria.getValue().toString()+"%");
            case ENDS_WITH ->  criteriaBuilder.like(root.get(searchCriteria.getKey()),"%"+searchCriteria.getValue().toString());
        };
    }
}
