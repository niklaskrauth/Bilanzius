package org.bilanzius.commands.implementations.deposit;

import java.util.Arrays;

public enum DepositCommandArguments
{
    DEPOSIT("amount", "-a");

    private final String argument;
    private final String argumentShort;

    DepositCommandArguments(String argument, String argumentShort)
    {
        this.argument = argument;
        this.argumentShort = argumentShort;
    }

    public String getArgument()
    {
        return argument;
    }

    public String getArgumentShort()
    {
        return argumentShort;
    }

    public static String getAllArguments()
    {
        return Arrays.stream(DepositCommandArguments.values()).map(
            a -> a.getArgument() + " ( " + a.getArgumentShort() + " ) "
        ).reduce(
            (a, b) -> a + ", " + b
        ).orElse("");
    }

    public static DepositCommandArguments fromString(String argument)
    {
        for (DepositCommandArguments c : DepositCommandArguments.values())
            {
                if (c.argument.equals(argument) || c.argumentShort.equals(argument))
                    {
                        return c;
                    }
            }
        return null;
    }

}
