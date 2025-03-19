package org.bilanzius.services.commands.convert;

import java.util.Arrays;

public enum ConvertCommandArguments {
    BITCOIN("bitcoin", "-btc"),
    GERMAN_DEUTSCHE_MARK("germanDeutscheMark", "-gdm"),
    SWISS_FRANC("swissFranc", "-chf"),
    DOGECOIN("dogecoin", "-doge"),
    ETHEREUM("ethereum", "-eth"),
    HONG_KONG_DOLLAR("hongKongDollar", "-hkd"),
    JAMAICAN_DOLLAR("jamaicanDollar", "-jmd"),
    NORTH_KOREAN_WON("northKoreanWon", "-kpw"),
    RUSSIAN_RUBLE("russianRuble", "-rub"),
    US_DOLLAR("usdDollar", "-usd"),
    PORSCHE_911_CAMERA("porsche911Camera", "-p911c"),
    WASHING_MACHINE("washingMachine", "-wm"),
    BANANA("banana", "-b");

    private final String argument;
    private final String argumentShort;

    ConvertCommandArguments(String argument, String argumentShort) {
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
        return Arrays.stream(ConvertCommandArguments.values()).map(
                a -> a.getArgument() + " ( " + a.getArgumentShort() + " ) "
        ).reduce(
                (a, b) -> a + ", " + b
        ).orElse("");
    }

    public static ConvertCommandArguments fromString(String argument) {
        for (ConvertCommandArguments c : ConvertCommandArguments.values()) {
            if (c.argument.equals(argument) || c.argumentShort.equals(argument)) {
                return c;
            }
        }
        return null;
    }

}
