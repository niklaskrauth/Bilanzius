package org.bilanzius.services.commands.getCategory;

import java.util.Arrays;

public enum GetCategoryCommandArguments {
    NAME("name", "-n"),
    ALL("all", "-a"),
    EXCEEDED("exceeded", "-e");

    private final String argument;
    private final String argumentShort;

    GetCategoryCommandArguments(String argument, String argumentShort) {
        this.argument = argument;
        this.argumentShort = argumentShort;
    }

    public String getArgument() {
        return argument;
    }

    public String getArgumentShort() {
        return argumentShort;
    }

    public static String getAllArguments() {
        return Arrays.stream(GetCategoryCommandArguments.values()).map(
                a -> a.getArgument() + " ( " + a.getArgumentShort() + " ) "
        ).reduce(
                (a, b) -> a + ", " + b
        ).orElse("");
    }

    public static GetCategoryCommandArguments fromString(String argument) {
        for (GetCategoryCommandArguments c : GetCategoryCommandArguments.values()) {
            if (c.argument.equals(argument) || c.argumentShort.equals(argument)) {
                return c;
            }
        }
        return null;
    }
}
