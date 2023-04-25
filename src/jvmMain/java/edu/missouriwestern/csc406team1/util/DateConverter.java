package edu.missouriwestern.csc406team1.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static String convertDateToString(LocalDate date) {
        return formatter.format(date);
    }

    public static LocalDate convertStringToDate(String date) {
        return LocalDate.parse(date, formatter);
    }
}
