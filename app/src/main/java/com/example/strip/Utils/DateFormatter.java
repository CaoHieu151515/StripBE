package com.example.strip.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    public static String formatDate(String inputDate) {
    String[] possibleFormats = {
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", // Case with milliseconds
            "yyyy-MM-dd'T'HH:mm:ss'Z'"        // Case without milliseconds
    };

    for (String format : possibleFormats) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(format, Locale.getDefault());
            inputFormat.setLenient(false); // Ensure strict parsing

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException ignored) {
            // Try next format
        }
    }
    return "Invalid Date"; // If none of the formats work
    }
}