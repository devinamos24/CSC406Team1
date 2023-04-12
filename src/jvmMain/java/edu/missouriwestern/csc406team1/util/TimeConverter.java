package edu.missouriwestern.csc406team1.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

    public static String convertTimeToString(LocalTime time) {
        return formatter.format(time);
    }

    public static LocalTime convertStringToTime(String time) {
        return LocalTime.parse(time, formatter);
    }
}
