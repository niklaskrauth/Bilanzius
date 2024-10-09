package org.bilanzius.services.commands;

public class HelpCommandService {

    public HelpCommandService() {}

    public String getAllCommands() {
        return """
                /help - Show all commands
                /exit - Exit the application
                """;
    }
}
