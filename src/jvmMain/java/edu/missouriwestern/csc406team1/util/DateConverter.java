package edu.missouriwestern.csc406team1.util;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConverter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static String convertDateToString(LocalDate date) {
        // TODO: implement date convert to string
        return formatter.format(date);
    }

    public static LocalDate convertStringToDate(String date) {
        // TODO: implement date convert to string
        return LocalDate.parse(date, formatter);
    }
}
