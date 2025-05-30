package org.bilanzius.commands.implementations.history;

import org.bilanzius.commands.Command;

import java.util.List;

public class HistoryCommand implements Command
{

    private List<String> history;

    public HistoryCommand(List<String> history)
    {
        this.history = history;
    }

    @Override
    public String execute(String[] arguments)
    {
        return createHistoryMessage();
    }

    private String createHistoryMessage()
    {
        return history.stream().map(s -> s + "\n").reduce("", String::concat).trim();
    }
}
