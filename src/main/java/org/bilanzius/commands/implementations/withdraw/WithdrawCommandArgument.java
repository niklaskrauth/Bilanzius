package org.bilanzius.commands.implementations.withdraw;

import java.util.Arrays;

public enum WithdrawCommandArgument
{
    WITHDRAW("amount", "-a"),
    CATEGORY("category",
            "-c");

    private final String argument;
    private final String argumentShort;

    WithdrawCommandArgument(String argument, String argumentShort)
    {
        this.argument =
                argument;
        this.argumentShort
                =
                argumentShort;
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
        return Arrays.stream(WithdrawCommandArgument.values()).map(
                a -> a.getArgument() + " ( " + a.getArgumentShort() + " ) "
        ).reduce(
                (a, b) -> a + ", " + b
        ).orElse("");
    }

    public static WithdrawCommandArgument fromString(String argument)
    {
        for (WithdrawCommandArgument c : WithdrawCommandArgument.values()) {
            if (c.argument.equals(argument) || c.argumentShort.equals(argument)) {
                return c;
            }
        }
        return null;
    }

}
