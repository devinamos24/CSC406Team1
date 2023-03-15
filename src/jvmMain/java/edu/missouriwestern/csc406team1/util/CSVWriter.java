package edu.missouriwestern.csc406team1.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is a utility class for converting object to csv records and writing them to disk
 */
public class CSVWriter {

    // This method handles escaping any special characters when we want to write them to disk
    private static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    // This method converts a "CSV" object to a csv record for writing to disk
    private static String convertToCSV(CSV record) {
        return Stream.of(record.convertToCSV())
                .map(CSVWriter::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    // This method attempts to write a list of "CSV" objects to disk
    // If the filename is taken it will attempt to wipe the file, then it will write to disk
    public static boolean writeToCsvFile(List<? extends CSV> records, String filename) throws IOException {
        File csvOutputFile = Paths.get("src", "jvmMain", "resources", filename).toFile();
        if (csvOutputFile.exists()) {
            if (!csvOutputFile.delete()) {
                return false;
            }
        }
        if (!csvOutputFile.createNewFile()) {
            return false;
        }
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            records.stream()
                    .map(CSVWriter::convertToCSV)
                    .forEach(pw::println);
        }
        return true;
    }
}
