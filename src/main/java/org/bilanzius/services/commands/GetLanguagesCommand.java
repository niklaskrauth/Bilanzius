package org.bilanzius.services.commands;

import org.bilanzius.utils.Localization;
import org.bilanzius.services.Command;

public class GetLanguagesCommand  implements Command {

    private final Localization localization = Localization.getInstance();

    @Override
    public String execute(String[] arguments) {
        return localization.getMessage("available_languages");
    }
}
