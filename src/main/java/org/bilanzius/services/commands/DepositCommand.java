package org.bilanzius.services.commands;

import org.bilanzius.User;
import org.bilanzius.services.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DepositCommand implements Command {

    private User user;
    private final Map<DepositCommandArguments, Function<String, String>> commandMap;


    public DepositCommand(User user) {
        this.user = user;

        commandMap = new HashMap<>();
        commandMap.put(DepositCommandArguments.DEPOSIT, this::depositMoney);

    }

    @Override
    public String execute(String[] arguments) {

        if (arguments == null || arguments.length == 0) {
            return "No arguments provided. Available arguments: " + DepositCommandArguments.getAllArguments();
        }

        DepositCommandArguments argument = DepositCommandArguments.fromString(arguments[0]);
        if (argument == null) {
            return "Unknown argument. Available arguments: " + DepositCommandArguments.getAllArguments();
        }

        Function<String, String> command = commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments.length > 1 ? arguments[1] : null);
        }

        return "Unknown argument. Available arguments: " + DepositCommandArguments.getAllArguments();
    }

    private String depositMoney(String argument) {

        double depositMoney;

        try {

            depositMoney = Double.parseDouble(argument);
            user.setBalance(user.getBalance() + depositMoney);

            return "Money successfully deposited. Your new balance is: " + user.getBalance();
        } catch (NumberFormatException e) {
            return "Invalid amount. Please provide a valid number.";
        }
    }
}
