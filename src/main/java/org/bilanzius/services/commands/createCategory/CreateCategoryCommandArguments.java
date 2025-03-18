package org.bilanzius.services.commands.createCategory;

import java.util.Arrays;

public enum CreateCategoryCommandArguments
{
    NAME("name", "-n"),
    BUDGET("budget", "-b");

    private final String argument;
    private final String argumentShort;

    CreateCategoryCommandArguments(String argument, String argumentShort)
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
        return Arrays.stream(CreateCategoryCommandArguments.values()).map(
                a -> a.getArgument() + " ( " + a.getArgumentShort() + " ) "
        ).reduce(
                (a, b) -> a + ", " + b
        ).orElse("");
    }

    public static CreateCategoryCommandArguments fromString(String argument)
    {
        for (CreateCategoryCommandArguments c : CreateCategoryCommandArguments.values()) {
            if (c.argument.equals(argument) || c.argumentShort.equals(argument)) {
                return c;
            }
        }
        return null;
    }
}
