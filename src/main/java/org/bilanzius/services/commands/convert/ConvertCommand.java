package org.bilanzius.services.commands.convert;

import com.google.gson.JsonObject;
import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteBankAccountService;
import org.bilanzius.services.Command;
import org.bilanzius.services.commands.BankAccountAware;
import org.bilanzius.utils.Localization;
import org.bilanzius.utils.Requests;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ConvertCommand implements Command, BankAccountAware {

    private final Map<ConvertCommandArguments, Supplier<String>> commandMap;
    private final Localization localization = Localization.getInstance();
    private final BankAccountService bankAccountService;
    private BankAccount selectedBankAccount;

    // TODO: Move this ito the .env file
    private final String currencyUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json";

    // Price of food products: https://prices.openfoodfacts.org/api/docs#/

    public ConvertCommand(SqlBackend backend, BankAccount selectedBankAccount) throws SQLException {
        this.bankAccountService = SqliteBankAccountService.getInstance(backend);
        this.selectedBankAccount = selectedBankAccount;

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
    public void setSelectedBankAccount(BankAccount bankAccount) {
        this.selectedBankAccount = bankAccount;
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
        double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        double part = balance / porsche911CameraPrice;
        return localization.getMessage("convert_buy_part_of", part, "Porsche 911 Camera");
    }

    private String convertToBitcoin() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "btc");
        double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        return localization.getMessage("convert_balance", "Bitcoin", (balance * exchangeRate));
    }

    private String convertToGermanDeutscheMark() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "dem");
        double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        return localization.getMessage("convert_balance", "Deutsche Mark", (balance * exchangeRate));
    }

    private String convertToSwissFranc() {

        String currency = "Swiss Franc";
        if (localization.getCurrentLanguageCode().equals("de")) {
            currency = "Schweizer Franken";
        }

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "chf");
        double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();

        return localization.getMessage("convert_balance", currency, (balance * exchangeRate));
    }

    private String convertToDogecoin() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "doge");
        double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        return localization.getMessage("convert_balance", "Dogecoin", (balance * exchangeRate));
    }

    private String convertToEthereum() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "eth");
        double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        return localization.getMessage("convert_balance", "Ethereum", (balance * exchangeRate));
    }

    private String convertToHongKongDollar() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "hkd");
        double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        return localization.getMessage("convert_balance", "Hong Kong Dollar", (balance * exchangeRate));
    }

    private String convertToJamaicanDollar() {

        String currency = "Jamaican Dollar";
        if (localization.getCurrentLanguageCode().equals("de")) {
            currency = "Jamaikanische Dollar";
        }

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "jmd");
        double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        return localization.getMessage("convert_balance", currency, (balance * exchangeRate));
    }

    private String convertToNorthKoreanWon() {

        String currency = "North Korean Won";
        if (localization.getCurrentLanguageCode().equals("de")) {
            currency = "Nordkoreanische Won";
        }

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "kpw");
        double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        return localization.getMessage("convert_balance", currency, (balance * exchangeRate));
    }

    private String convertToRussianRuble() {

        String currency = "Russian Ruble";
        if (localization.getCurrentLanguageCode().equals("de")) {
            currency = "Russischer Rubel";
        }

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "rub");
        double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        return localization.getMessage("convert_balance", currency, (balance * exchangeRate));
    }

    private String convertToUsDollar() {

        JsonObject jsonObject = Requests.getRequest(currencyUrl);
        assert jsonObject != null;
        double exchangeRate = getCurrencyFromJson(jsonObject, "usd");
        double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        return localization.getMessage("convert_balance", "US Dollar", (balance * exchangeRate));
    }

    private double getCurrencyFromJson(JsonObject jsonObject, String currencyShort) {
        return jsonObject.get("eur").getAsJsonObject().get(currencyShort).getAsDouble();
    }
}
