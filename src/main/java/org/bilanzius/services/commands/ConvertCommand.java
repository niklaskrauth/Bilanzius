package org.bilanzius.services.commands;

import com.google.gson.JsonObject;
import org.bilanzius.User;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;
import org.bilanzius.utils.Requests;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class ConvertCommand implements Command {

    private User user;
    private final Map<ConvertCommandArguments, Supplier<String>> commandMap;
    private final Localization localization = Localization.getInstance();

    // TODO: Move this ito the .env file
    private final String currencyUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json";

    // TODO: Figure this out https://prices.openfoodfacts.org/api/docs#/
    // And get the price of bananas in EUR

    public ConvertCommand(User user) {
        this.user = user;

        commandMap = new HashMap<>();
        commandMap.put(ConvertCommandArguments.BITCOIN, this::convertToBitcoin);
        commandMap.put(ConvertCommandArguments.GERMAN_DEUTSCHE_MARK, this::convertToGermanDeutscheMark);
        commandMap.put(ConvertCommandArguments.SWISS_FRANC, this::convertToSwissFranc);
        commandMap.put(ConvertCommandArguments.DOGECOIN, this::convertToDogecoin);
        commandMap.put(ConvertCommandArguments.ETHEREUM, this::convertToEthereum);
        commandMap.put(ConvertCommandArguments.HONG_KONG_DOLLAR, this::convertToHongKongDollar);
        commandMap.put(ConvertCommandArguments.JAMAICAN_DOLLAR, this::convertToJamaicanDollar);
        commandMap.put(ConvertCommandArguments.NORTH_KOREAN_WON, this::convertToNorthKoreanWon);
        commandMap.put(ConvertCommandArguments.RUSSIAN_RUBLE, this::convertToRussianRuble);
        commandMap.put(ConvertCommandArguments.US_DOLLAR, this::convertToUsDollar);
        commandMap.put(ConvertCommandArguments.PORSCHE_911_CAMERA, this::convertToPorsche911Camera);
    }

    @Override
    public String execute(String[] arguments) {

        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided",
                    ConvertCommandArguments.getAllArguments());
        }

        ConvertCommandArguments argument = ConvertCommandArguments.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument",
                    ConvertCommandArguments.getAllArguments());
        }

        Supplier<String> command = commandMap.get(argument);
        if (command != null) {
            return command.get();
        }

        return localization.getMessage("unknown_argument", ConvertCommandArguments.getAllArguments());
    }

    private String convertToPorsche911Camera() {

        int porsche911CameraPrice = 128700;
        double part = user.getBalance() / porsche911CameraPrice;
        return localization.getMessage("convert_buy_part_of", part, "Porsche 911 Camera");
    }

    private String convertToBitcoin() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "btc");
        return localization.getMessage("convert_balance", "Bitcoin", (user.getBalance() * exchangeRate));
    }

    private String convertToGermanDeutscheMark() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "dem");
        return localization.getMessage("convert_balance", "German Deutsche Mark", (user.getBalance() * exchangeRate));
    }

    private String convertToSwissFranc() {

        String currency = "Swiss Franc";
        if (localization.getCurrentLanguageCode().equals("de")) {
            currency = "Schweizer Franken";
        }

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "chf");

        return localization.getMessage("convert_balance", currency, (user.getBalance() * exchangeRate));
    }

    private String convertToDogecoin() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "doge");
        return localization.getMessage("convert_balance", "Dogecoin", (user.getBalance() * exchangeRate));
    }

    private String convertToEthereum() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "eth");
        return localization.getMessage("convert_balance", "Ethereum", (user.getBalance() * exchangeRate));
    }

    private String convertToHongKongDollar() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "hkd");
        return localization.getMessage("convert_balance", "Hong Kong Dollar", (user.getBalance() * exchangeRate));
    }

    private String convertToJamaicanDollar() {

        String currency = "Jamaican Dollar";
        if (localization.getCurrentLanguageCode().equals("de")) {
            currency = "Jamaikanische Dollar";
        }

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "jmd");
        return localization.getMessage("convert_balance", currency, (user.getBalance() * exchangeRate));
    }

    private String convertToNorthKoreanWon() {

        String currency = "North Korean Won";
        if (localization.getCurrentLanguageCode().equals("de")) {
            currency = "Nordkoreanischer Won";
        }

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "kpw");
        return localization.getMessage("convert_balance", currency, (user.getBalance() * exchangeRate));
    }

    private String convertToRussianRuble() {

        String currency = "Russian Ruble";
        if (localization.getCurrentLanguageCode().equals("de")) {
            currency = "Russischer Rubel";
        }

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "rub");
        return localization.getMessage("convert_balance", currency, (user.getBalance() * exchangeRate));
    }

    private String convertToUsDollar() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "usd");
        return localization.getMessage("convert_balance", "US Dollar", (user.getBalance() * exchangeRate));
    }

    private double getCurrencyFromJson(JsonObject jsonObject, String currencyShort) {
        return jsonObject.get("eur").getAsJsonObject().get(currencyShort).getAsDouble();
    }
}
