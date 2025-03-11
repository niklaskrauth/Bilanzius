package org.bilanzius.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalizationTest {

    private static final String NON_EXISTING_KEY = "unit_non_existing_key";
    private static final String EXISTING_KEY = "login";
    private static final String EXISTING_KEY_VALUE = "Login";

    private static final String EXISTING_KEY_WITH_PARAMS = "withdraw_successful";
    private static final String EXISTING_KEY_WITH_PARAMS_VALUE = "Withdrawal successful. Your new balance is 1.";

    @Test
    void testSupportedLanguages() {
        var language = Localization.getInstance();
        Assertions.assertTrue(language.isSupportedLanguage("en"));
        Assertions.assertFalse(language.isSupportedLanguage("invalid"));
    }

    @Test
    void testGetMessage() {
        var language = Localization.getInstance();
        language.setLocale("en");

        Assertions.assertEquals("en", language.getCurrentLanguageCode());
        Assertions.assertEquals(EXISTING_KEY_VALUE, language.getMessage(EXISTING_KEY));
        Assertions.assertEquals("MISSING KEY: " + NON_EXISTING_KEY, language.getMessage(NON_EXISTING_KEY));
    }

    @Test
    void testGetMessageWithParams() {
        var language = Localization.getInstance();
        language.setLocale("en");

        Assertions.assertEquals("en", language.getCurrentLanguageCode());
        Assertions.assertEquals(EXISTING_KEY_WITH_PARAMS_VALUE, language.getMessage(EXISTING_KEY_WITH_PARAMS, "1"));
    }
}
