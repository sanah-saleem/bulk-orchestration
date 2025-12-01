package com.project.bulkorchestration.utils;

public class CsvUtils {

    private CsvUtils() {
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        String v = value;
        boolean hasQuote = v.contains("\"");
        boolean hasComma = v.contains(",");
        boolean hasNewline = v.contains("\n") || v.contains("\r");

        if (hasQuote) {
            v = v.replace("\"", "\"\""); // escape inner quotes
        }
        if (hasComma || hasNewline || hasQuote) {
            v = "\"" + v + "\"";
        }
        return v;
    }

}
