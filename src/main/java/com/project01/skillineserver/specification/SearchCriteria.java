package com.project01.skillineserver.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;


    public SearchCriteria(String key,String operations,Object value,String prefix,String suffix){
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operations.charAt(0));
        this.key = key;
        this.value = value;
        this.operation = searchOperation;
    }
}
