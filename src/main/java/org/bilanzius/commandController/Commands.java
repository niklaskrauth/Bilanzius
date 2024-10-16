package org.bilanzius.commandController;

import org.bilanzius.services.commands.bilanziusCommand.BilanziusCommandArguments;

import java.util.Arrays;

public enum Commands {
    EXIT("/exit", "Exit the application", null),
    HELP("/help", "Show all commands", null),
    BILANZIUS("/bilanzius", "Get information about the application", BilanziusCommandArguments.getAllArguments());

    // Hier werden die einzelnen Befehle hinzugefügt

    private final String command;
    private final String description;
    private final String arguments;

    Commands(String command, String description, String arguments) {
        this.command = command;
        this.description = description;
        this.arguments = arguments;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String getArguments() {
        return arguments;
    }

    public static String getAllCommands() {

        return Arrays.stream(Commands.values()).map(
                c -> c.getCommand()
                        + " - " +
                        c.getDescription()
                        +
                        (
                                c.getArguments()
                                        != null ?
                                        " | " + (c.getArguments())
                                        : ""
                        )
                ).reduce(
                        (a, b) -> a + "\n" + b
                ).orElse("");
    }

    public static Commands fromString(String command) {
        for (Commands c : Commands.values()) {
            if (c.command.equals(command)) {
                return c;
            }
        }
        return null;
    }

}