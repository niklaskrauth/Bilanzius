package org.bilanzius.services.commands.bilanzius;

import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BilanziusCommand implements Command
{
    private final Map<BilanziusCommandArguments, Supplier<String>> commandMap = new HashMap<>();
    private final Localization localization = Localization.getInstance();

    public BilanziusCommand()
    {
        commandMap.put(BilanziusCommandArguments.VERSION, this::getVersion);
        commandMap.put(BilanziusCommandArguments.AUTHORS, this::getAuthors);
        commandMap.put(BilanziusCommandArguments.DESCRIPTION, this::getDescription);
    }

    @Override
    public String execute(String[] arguments)
    {

        BilanziusCommandArguments argument;
        Supplier<String> command;

        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided", BilanziusCommandArguments.getAllArguments());
        }

        argument =
                BilanziusCommandArguments.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument", BilanziusCommandArguments.getAllArguments());
        }

        command =
                commandMap.get(argument);
        if (command != null) {
            return command.get();
        }

        return localization.getMessage("unknown_argument", BilanziusCommandArguments.getAllArguments());
    }

    //    TODO: get version
    //     from .env datei
    private String getVersion()
    {
        return localization.getMessage("version", "0.1.3");
    }

    private String getAuthors()
    {
        return localization.getMessage("authors_list");
    }

    private String getDescription()
    {
        return localization.getMessage("bilanzius_description");
    }
}