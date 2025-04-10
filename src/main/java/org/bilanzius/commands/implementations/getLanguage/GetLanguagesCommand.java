package org.bilanzius.commands.implementations.getLanguage;

import org.bilanzius.utils.Localization;
import org.bilanzius.commands.Command;

public class GetLanguagesCommand implements Command
{
    private final Localization localization = Localization.getInstance();

    @Override
    public String execute(String[] arguments)
    {
        return localization.getMessage("available_languages");
    }
}
