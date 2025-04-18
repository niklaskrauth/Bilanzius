package org.bilanzius.commands.implementations.setLanguage;

import org.bilanzius.utils.Localization;
import org.bilanzius.commands.Command;

public class SetLanguageCommand implements Command
{

    private final Localization localization = Localization.getInstance();

    @Override
    public String execute(String[] arguments)
    {

        String languageCode;

        if (arguments.length != 1) {
            return localization.getMessage("set_language_usage");
        }
        languageCode =
                arguments[0];

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
