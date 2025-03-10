package org.bilanzius.utils;

import java.text.MessageFormat;
import java.util.*;

public class Localization {
    private static Localization instance;
    private final List<String> supportedLanguages = Arrays.asList("en", "de");
    private ResourceBundle messages;
    private String currentLanguageCode;

    private Localization(String languageCode) {
        setLocale(languageCode);
    }

    public boolean isSupportedLanguage(String languageCode) {
        return supportedLanguages.contains(languageCode);
    }

    public static Localization getInstance() {
        if (instance == null) {
            instance = new Localization("en"); // Standard auf Englisch
        }
        return instance;
    }

    public void setLocale(String languageCode) {
        this.currentLanguageCode = languageCode;
        Locale locale = new Locale(languageCode);
        messages = ResourceBundle.getBundle("messages", locale);
    }

    public String getCurrentLanguageCode() {
        return this.currentLanguageCode;
    }

    public String getMessage(String key, Object... params) {
        try {
            String message = messages.getString(key);
            if (params != null && params.length > 0) {
                message = MessageFormat.format(message, params);
            }
            return message;
        } catch (MissingResourceException ex) {
            return "MISSING KEY: " + key;
        }
    }
}
