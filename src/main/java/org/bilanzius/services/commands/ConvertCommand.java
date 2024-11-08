package org.bilanzius.services.commands;

import com.google.gson.JsonObject;
import org.bilanzius.User;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;
import org.bilanzius.utils.Requests;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ConvertCommand implements Command {

    private User user;
    private final Map<ConvertCommandArguments, Supplier<String>> commandMap;
    private final Localization localization = Localization.getInstance();

    // TODO. Move this ito the .env file
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
        return "You can buy this much of a Porsche 911 Camera: " + user.getBalance() / porsche911CameraPrice;
    }

    private String convertToBitcoin() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "btc");

        return "Your Balance in Bitcoin is: " + user.getBalance() * exchangeRate;
    }

    private String convertToGermanDeutscheMark() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "dem");

        return "Your Balance in German Deutsche Mark is: " + user.getBalance() * exchangeRate;
    }

    private String convertToSwissFranc() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "chf");

        return "Your Balance in Swiss Franc is: " + user.getBalance() * exchangeRate;
    }

    private String convertToDogecoin() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "doge");

        return "Your Balance in Dogecoin is: " + user.getBalance() * exchangeRate;
    }

    private String convertToEthereum() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "eth");

        return "Your Balance in Ethereum is: " + user.getBalance() * exchangeRate;
    }

    private String convertToHongKongDollar() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "hkd");

        return "Your Balance in Hong Kong Dollar is: " + user.getBalance() * exchangeRate;
    }

    private String convertToJamaicanDollar() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "jmd");

        return "Your Balance in Jamaican Dollar is: " + user.getBalance() * exchangeRate;
    }

    private String convertToNorthKoreanWon() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "kpw");

        return "Your Balance in North Korean Won is: " + user.getBalance() * exchangeRate;
    }

    private String convertToRussianRuble() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "rub");

        return "Your Balance in Russian Rupee is: " + user.getBalance() * exchangeRate;
    }

    private String convertToUsDollar() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "usd");

        return "Your Balance in USD is: " + user.getBalance() * exchangeRate;
    }

    private double getCurrencyFromJson(JsonObject jsonObject, String currencyShort) {
        return jsonObject.get("eur").getAsJsonObject().get(currencyShort).getAsDouble();
    }
}
