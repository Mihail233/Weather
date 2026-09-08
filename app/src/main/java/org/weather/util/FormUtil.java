package org.weather.util;

import lombok.experimental.UtilityClass;
import org.thymeleaf.spring6.util.DetailedError;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class FormUtil {
    private static final String delimiter = ". ";

    public static String toString(List<DetailedError> errors) {
        List<String> messages = new ArrayList<>();
        for (DetailedError error: errors) {
            messages.add(error.getMessage());
        }
        return String.join(delimiter, messages);
    }
}
