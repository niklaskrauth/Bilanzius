package org.bilanzius.services.commands.setLanguage;

import org.bilanzius.utils.Localization;
import org.bilanzius.services.Command;

public class SetLanguageCommand implements Command {
    private final Localization localization = Localization.getInstance();

    @Override
    public String execute(String[] arguments) {

        if (arguments.length != 1) {
            return localization.getMessage("set_language_usage");
        }
        String languageCode = arguments[0];

        if (!localization.isSupportedLanguage(languageCode)) {
            return localization.getMessage("unsupported_language");
        }

        if (localization.getCurrentLanguageCode().equals(languageCode)) {
            return localization.getMessage("language_already_set", languageCode);
        }

        localization.setLocale(languageCode);
        return localization.getMessage("language_changed");
    }
}
