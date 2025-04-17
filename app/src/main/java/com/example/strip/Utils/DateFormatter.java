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
    public static String formatDatePrimary(String inputDate) {
        String[] possibleFormats = {
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", // Case with milliseconds
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        };

        for (String format : possibleFormats) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat(format, Locale.getDefault());
                inputFormat.setLenient(false); // Ensure strict parsing

                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = inputFormat.parse(inputDate);
                return outputFormat.format(date);
            } catch (ParseException ignored) {
                // Try next format
            }
        }
        return "Invalid Date"; // If none of the formats work
    }
    public static String formatDatePrimaryStore(String inputDate) {
        String[] possibleFormats = {
                "MM/dd/yyyy'\n'hh:mm:ss a'", // Case with milliseconds
                "dd/MM/yyyy'",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        };

        for (String format : possibleFormats) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat(format, Locale.getDefault());
                inputFormat.setLenient(false); // Ensure strict parsing

                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                Date date = inputFormat.parse(inputDate);
                return outputFormat.format(date);
            } catch (ParseException ignored) {
                // Try next format
            }
        }
        return "Invalid Date"; // If none of the formats work
    }
}