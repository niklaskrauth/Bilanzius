package org.bilanzius.services.commands.deleteCategory;

import java.util.Arrays;

public enum DeleteCategoryCommandArguments {
    NAME("name", "-n"),
    ALL("all", "-a");

    private final String argument;
    private final String argumentShort;

    DeleteCategoryCommandArguments(String argument, String argumentShort) {
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
        return Arrays.stream(DeleteCategoryCommandArguments.values()).map(
                a -> a.getArgument() + " ( " + a.getArgumentShort() + " ) "
        ).reduce(
                (a, b) -> a + ", " + b
        ).orElse("");
    }

    public static DeleteCategoryCommandArguments fromString(String argument) {
        for (DeleteCategoryCommandArguments c : DeleteCategoryCommandArguments.values()) {
            if (c.argument.equals(argument) || c.argumentShort.equals(argument)) {
                return c;
            }
        }
        return null;
    }

}
