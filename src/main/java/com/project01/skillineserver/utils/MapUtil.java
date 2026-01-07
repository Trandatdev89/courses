package com.project01.skillineserver.utils;

import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.excepion.CustomException.AppException;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapUtil {
    public static <X,Y> Map<X,Y> extractInfo(Object dataNeedExtract) throws IllegalAccessException {

        if(Objects.isNull(dataNeedExtract)){
            throw new AppException(ErrorCode.INTERNAL_SERVER);
        }

        Map<X,Y> infoExtract = new HashMap<>();
        Field[] fields = dataNeedExtract.getClass().getDeclaredFields();
        for (Field field : fields){
            field.setAccessible(true);
            String key = field.getName();
            Object value = field.get(dataNeedExtract);
            if(value!=null){
                if(value instanceof String){
                    if(StringUtils.hasText((CharSequence) value)){
                        infoExtract.put((X)key,(Y)value);
                    }
                }else{
                    infoExtract.put((X)key,(Y)value);
                }
            }
        }
        return infoExtract;
    }
}
