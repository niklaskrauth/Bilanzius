package org.bilanzius.services.commands;

import org.bilanzius.services.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BilanziusCommand implements Command {

    private final Map<BilanziusCommandArguments, Supplier<String>> commandMap;

    public BilanziusCommand() {
        commandMap = new HashMap<>();

        commandMap.put(BilanziusCommandArguments.VERSION, this::getVersion);
        commandMap.put(BilanziusCommandArguments.AUTHORS, this::getAuthors);
        commandMap.put(BilanziusCommandArguments.DESCRIPTION, this::getDescription);
    }

    @Override
    public String execute(String[] arguments) {

        if (arguments == null || arguments.length == 0) {
            return "No arguments provided. Available arguments: " + BilanziusCommandArguments.getAllArguments();
        }

        BilanziusCommandArguments argument = BilanziusCommandArguments.fromString(arguments[0]);
        if (argument == null) {
            return "Unknown argument. Available arguments: " + BilanziusCommandArguments.getAllArguments();
        }

        Supplier<String> command = commandMap.get(argument);
        if (command != null) {
            return command.get();
        }

        return "Unknown argument. Available arguments: " + BilanziusCommandArguments.getAllArguments();
    }

    private String getVersion() {
        return "Bilanzius version: 0.1.0";
    }

    private String getAuthors() {
        return "Authors: Lukas Hertkorn, Niklas Krauth, Lukas Melcher";
    }

    private String getDescription() {
        return "Bilanzius is a simple application that helps you to manage your finances.";
    }
}