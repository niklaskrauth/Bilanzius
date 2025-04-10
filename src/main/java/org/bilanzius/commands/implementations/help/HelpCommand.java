package org.bilanzius.commands.implementations.help;

import org.bilanzius.commands.Command;

import static org.bilanzius.commands.Commands.getAllCommands;

public class HelpCommand implements Command
{

    @Override
    public String execute(String[] arguments)
    {
        return getAllCommands();
    }
}