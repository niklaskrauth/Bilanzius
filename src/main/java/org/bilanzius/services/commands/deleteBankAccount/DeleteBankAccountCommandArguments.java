package org.bilanzius.services.commands.deleteBankAccount;

import java.util.Arrays;

public enum DeleteBankAccountCommandArguments
{
    NAME("name", "-n"),
    ALL("all", "-a");

    private final String argument;
    private final String argumentShort;

    DeleteBankAccountCommandArguments(String argument, String argumentShort)
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
        return Arrays.stream(DeleteBankAccountCommandArguments.values()).map(
                a -> a.getArgument() + " ( " + a.getArgumentShort() + " ) "
        ).reduce(
                (a, b) -> a + ", " + b
        ).orElse("");
    }

    public static DeleteBankAccountCommandArguments fromString(String argument)
    {
        for (DeleteBankAccountCommandArguments c : DeleteBankAccountCommandArguments.values())
        {
            if (c.argument.equals(argument) || c.argumentShort.equals(argument))
            {
                return c;
            }
        }
        return null;
    }
}
