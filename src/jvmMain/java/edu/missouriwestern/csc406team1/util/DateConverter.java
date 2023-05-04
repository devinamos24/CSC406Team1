package edu.missouriwestern.csc406team1.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter specialFormatter = DateTimeFormatter.ofPattern("MMddyyyy");
    public static String convertDateToString(LocalDate date) {
        return formatter.format(date);
    }

    public static LocalDate convertStringToDate(String date) {
        return LocalDate.parse(date, formatter);
    }

    public static LocalDate convertStringToDateInterface(String date) {
        return LocalDate.parse(date, specialFormatter);
    }

    public static String convertDateToStringInterface(LocalDate date) {
        return specialFormatter.format(date);
    }
}
