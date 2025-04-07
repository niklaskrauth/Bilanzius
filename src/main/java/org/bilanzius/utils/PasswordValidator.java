package org.bilanzius.utils;

import java.util.regex.Pattern;

public class PasswordValidator
{

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
    // Needs to contain: 8
    // chars, 1 digit, 1
    // lowercase, 1
    // uppercase, 1 special
    // char

    public static boolean validatePassword(String password)
    {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }

}
