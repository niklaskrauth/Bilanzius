package org.bilanzius.commands.implementations.bilanzius;

import java.util.Arrays;

public enum BilanziusCommandArguments
{
    VERSION("-version", "-v"),
    AUTHORS("-authors", "-a"),
    DESCRIPTION(
            "-description",
            "-d");

    private final String argument;
    private final String argumentShort;

    BilanziusCommandArguments(String argument, String argumentShort)
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

        return Arrays.stream(BilanziusCommandArguments.values()).map(
                a -> a.getArgument() + " ( " + a.getArgumentShort() + " ) "
        ).reduce(
                (a, b) -> a + ", " + b
        ).orElse("");

    }

    public static BilanziusCommandArguments fromString(String argument)
    {
        for (BilanziusCommandArguments c : BilanziusCommandArguments.values()) {
            if (c.argument.equals(argument) || c.argumentShort.equals(argument)) {
                return c;
            }
        }
        return null;
    }
}
