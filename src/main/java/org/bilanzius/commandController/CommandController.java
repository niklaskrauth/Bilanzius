package org.bilanzius.commandController;

import org.bilanzius.services.commands.HelpCommandService;

public class CommandController {

    public CommandController() {}

    public String handleInput(String input) {

        // Jeder Befehl hat seine eigenen Case wenn es mehr als 1 Zeile Code ist
        // Dort wird dann immer eine Klasse aufgerufen um den Befehl zu verarbeiten
        // Es sollte vermieden werden in einem Code viel Logik zu haben, am besten nur eine Methode in der Klasse die dann alles handelt
        // In der Ordnerstruktur gibt es den "commands"-ordner, dort sind alle Klassen die die Befehle verarbeiten
        // Für andere Sachen sollten weiter Ordner erstellt werden (z.B. Datenbank etc.)
        // Beispielcode: /help -> HelpCommandService

        String output;

        switch (input) {
            case "/exit":
                System.exit(0);
                output = "Goodbye User";
                break;
            case "/help":
                HelpCommandService helpCommandService = new HelpCommandService();
                output = helpCommandService.getAllCommands();
                break;
            default:
                output = "Something went wrong :( ";
                break;
        }

        return output;
    }
}
