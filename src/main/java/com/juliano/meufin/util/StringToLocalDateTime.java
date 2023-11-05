package com.juliano.meufin.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateTime {
    public static LocalDateTime convert(String stringDate) {
        if(stringDate != null) {
            LocalDate localDate = LocalDate.parse(stringDate);
            return  LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
        }
        return  null;
    }
}
