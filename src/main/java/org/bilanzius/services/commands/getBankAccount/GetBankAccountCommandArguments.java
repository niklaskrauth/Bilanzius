package org.bilanzius.services.commands.getBankAccount;

import java.util.Arrays;

public enum GetBankAccountCommandArguments
{
    NAME("name", "-n"),
    ALL("all", "-a");

    private final String argument;
    private final String argumentShort;

    GetBankAccountCommandArguments(String argument, String argumentShort)
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
        return Arrays.stream(GetBankAccountCommandArguments.values()).map(
                a -> a.getArgument() + " ( " + a.getArgumentShort() + " ) "
        ).reduce(
                (a, b) -> a + ", " + b
        ).orElse("");
    }

    public static GetBankAccountCommandArguments fromString(String argument)
    {
        for (GetBankAccountCommandArguments c : GetBankAccountCommandArguments.values()) {
            if (c.argument.equals(argument) || c.argumentShort.equals(argument)) {
                return c;
            }
        }
        return null;
    }
}
