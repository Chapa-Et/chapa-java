package com.github.amenski.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.amenski.utility.StringUtils.isNotBlank;

public final class Validate {

    private Validate() {
        throw new IllegalStateException("");
    }

    public static boolean isValidEmail(final String token) {
        return isNotBlank(token) && validateRegex(token, "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,5}");
    }

    public static boolean isValidPhoneNumber(final String token) {
        return isNotBlank(token) && validateRegex(token, "^0[79]\\d{8}$");
    }

    private static boolean validateRegex(final String token, final String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(token);
        return matcher.matches();
    }
}
