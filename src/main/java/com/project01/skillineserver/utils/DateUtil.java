package com.project01.skillineserver.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DateUtil {

    Map<Long, Function<Instant,String>> strategyMap = new LinkedHashMap<>();

    public DateUtil(){
        strategyMap.put(60L,this::formatSecond);
        strategyMap.put(3600L,this::formatMinutes);
        strategyMap.put(86400L,this::formatHours);
        strategyMap.put(Long.MAX_VALUE,this::formatInDate);
    }

    public String format(Instant instant){
        long elapseSeconds = ChronoUnit.SECONDS.between(instant,Instant.now());

        var strategy = strategyMap.entrySet()
                .stream()
                .filter(item->elapseSeconds < item.getKey())
                .findFirst().get();
        return strategy.getValue().apply(instant);
    }

    private String formatSecond(Instant instant){
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        return String.format("%s second(s) ago", elapseSeconds);
    }

    private String formatMinutes(Instant instant){
        long elapseMinutes = ChronoUnit.MINUTES.between(instant, Instant.now());
        return String.format("%s minute(s) ago", elapseMinutes);
    }

    private String formatHours(Instant instant){
        long elapseHours = ChronoUnit.HOURS.between(instant, Instant.now());
        return String.format("%s hour(s) ago", elapseHours);
    }

    private String formatInDate(Instant instant){
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
        return localDateTime.format(dateTimeFormatter);
    }
}
