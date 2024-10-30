package org.bilanzius.services.commands;

import org.bilanzius.utils.Localization;
import org.bilanzius.services.Command;

public class GetLanguagesCommand  implements Command {

    @Override
    public String execute(String[] arguments) {
        Localization localization = Localization.getInstance();
        return localization.getMessage("available_languages");
    }
}
