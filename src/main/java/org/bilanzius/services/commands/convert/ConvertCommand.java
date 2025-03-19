package org.bilanzius.services.commands.convert;

import com.google.gson.JsonObject;
import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.services.Command;
import org.bilanzius.services.commands.BankAccountAware;
import org.bilanzius.utils.Localization;
import org.bilanzius.utils.Requests;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ConvertCommand implements Command, BankAccountAware {

    private final Map<ConvertCommandArguments, Supplier<String>> commandMap = new HashMap<>();
    private final Localization localization = Localization.getInstance();
    private final BankAccountService bankAccountService;
    private BankAccount selectedBankAccount;

    // TODO: Move this into the .env file
    private final String currencyUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json";

    public ConvertCommand(BankAccount selectedBankAccount) {
        this.bankAccountService = DatabaseProvider.getBankAccountService();
        this.selectedBankAccount = selectedBankAccount;

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
    public void setSelectedBankAccount(BankAccount bankAccount) {
        this.selectedBankAccount = bankAccount;
    }

    @Override
    public String execute(String[] arguments) {

        ConvertCommandArguments argument;
        Supplier<String> command;

        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided",
                    ConvertCommandArguments.getAllArguments());
        }

        argument = ConvertCommandArguments.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument",
                    ConvertCommandArguments.getAllArguments());
        }

        command = commandMap.get(argument);
        if (command != null) {
            return command.get();
        }

        return localization.getMessage("unknown_argument", ConvertCommandArguments.getAllArguments());
    }

    private String convertToPorsche911Camera() {

        BigDecimal porsche911CameraPrice = new BigDecimal("128700");
        BigDecimal balance;

        try {
            balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        } catch (DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }

        BigDecimal part = balance.divide(porsche911CameraPrice, RoundingMode.HALF_EVEN);
        return localization.getMessage("convert_buy_part_of", part, "Porsche 911 Camera");
    }

    private String convertCurrency(String currencyCode, String currencyName) {
        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        BigDecimal exchangeRate = BigDecimal.valueOf(getCurrencyFromJson(jsonObject, currencyCode));
        BigDecimal balance;

        try {
            balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        } catch (DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }

        return localization.getMessage("convert_balance", currencyName, (balance.multiply(exchangeRate)));
    }

    private String convertToBitcoin() {
        return convertCurrency("btc", "Bitcoin");
    }

    private String convertToGermanDeutscheMark() {
        return convertCurrency("dem", "Deutsche Mark");
    }

    private String convertToSwissFranc() {
        String currency = localization.getCurrentLanguageCode().equals("de") ? "Schweizer Franken" : "Swiss Franc";
        return convertCurrency("chf", currency);
    }

    private String convertToDogecoin() {
        return convertCurrency("doge", "Dogecoin");
    }

    private String convertToEthereum() {
        return convertCurrency("eth", "Ethereum");
    }

    private String convertToHongKongDollar() {
        return convertCurrency("hkd", "Hong Kong Dollar");
    }

    private String convertToJamaicanDollar() {
        String currency = localization.getCurrentLanguageCode().equals("de") ? "Jamaikanische Dollar" : "Jamaican Dollar";
        return convertCurrency("jmd", currency);
    }

    private String convertToNorthKoreanWon() {
        String currency = localization.getCurrentLanguageCode().equals("de") ? "Nordkoreanische Won" : "North Korean Won";
        return convertCurrency("kpw", currency);
    }

    private String convertToRussianRuble() {
        String currency = localization.getCurrentLanguageCode().equals("de") ? "Russischer Rubel" : "Russian Ruble";
        return convertCurrency("rub", currency);
    }

    private String convertToUsDollar() {
        return convertCurrency("usd", "US Dollar");
    }

    private double getCurrencyFromJson(JsonObject jsonObject, String currencyShort) {
        return jsonObject.get("eur").getAsJsonObject().get(currencyShort).getAsDouble();
    }
}
