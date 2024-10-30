package org.bilanzius.services.commands;

import org.bilanzius.utils.Localization;
import org.bilanzius.services.Command;

public class SetLanguageCommand implements Command {

    @Override
    public String execute(String[] arguments) {
        if (arguments.length == 1) {
            String languageCode = arguments[0];
            Localization localization = Localization.getInstance();
            if (localization.isSupportedLanguage(languageCode)) {
                localization.setLocale(languageCode);
                return localization.getMessage("language_changed");
            } else {
                return localization.getMessage("unsupported_language");
            }
        } else {
            return Localization.getInstance().getMessage("set_language_usage");
        }
    }
}
