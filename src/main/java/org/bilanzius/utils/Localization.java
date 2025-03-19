package org.bilanzius.utils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class Localization
{
    private static Localization instance;
    private final List<String> supportedLanguages = Arrays.asList("en", "de");

    private Locale locale;
    private ResourceBundle messages;
    private String currentLanguageCode;

    private Localization(String languageCode)
    {
        setLocale(languageCode);
    }

    public boolean isSupportedLanguage(String languageCode)
    {
        return supportedLanguages.contains(languageCode);
    }

    public static Localization getInstance()
    {
        if (instance == null) {
            instance =
                    new Localization("en"); // Standard auf Englisch
        }
        return instance;
    }

    public void setLocale(String languageCode)
    {
        this.currentLanguageCode = languageCode;
        this.locale =
                Locale.of(languageCode);
        this.messages =
                ResourceBundle.getBundle("messages", locale);
    }

    public String getCurrentLanguageCode()
    {
        return this.currentLanguageCode;
    }

    public String getMessage(String key, Object... params)
    {
        try {
            String message
                    =
                    this.messages.getString(key);
            if (params != null && params.length > 0) {
                message =
                        MessageFormat.format(message, params);
            }
            return message;
        } catch (
                MissingResourceException ex) {
            return "MISSING" +
                    " KEY: "
                    + key;
        }
    }

    public String formatInstant(Instant instant)
    {
        var dateFormat =
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)
                        .withLocale(this.locale)
                        .withZone(ZoneId.systemDefault());

        return dateFormat.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
    }

    public String formatCurrency(BigDecimal money)
    {
        return String.format("%,.2f €", money);
    }

    public Set<String> keySet()
    {
        return this.messages.keySet();
    }

}
