package com.example.taskmanager.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailValidator {
    private static final String MAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public static boolean isMail(String mail) {
        Pattern pattern = Pattern.compile(MAIL_REGEX);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }
}
