package org.bilanzius.services.commands;

import org.bilanzius.persistence.models.User;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class WithdrawCommand implements Command {

    private User user;
    private final Map<WithdrawCommandArgument, Function<String, String>> commandMap;
    private final Localization localization = Localization.getInstance();


    public WithdrawCommand(User user) {
        this.user = user;

        commandMap = new HashMap<>();
        commandMap.put(WithdrawCommandArgument.WITHDRAW, this::withdrawMoney);

    }

    @Override
    public String execute(String[] arguments) {

        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided",
                    WithdrawCommandArgument.getAllArguments());
        }

        WithdrawCommandArgument argument = WithdrawCommandArgument.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument",
                    WithdrawCommandArgument.getAllArguments());
        }

        Function<String, String> command = commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments.length > 1 ? arguments[1] : null);
        }

        return localization.getMessage("unknown_argument", WithdrawCommandArgument.getAllArguments());
    }

    private String withdrawMoney(String argument) {

        double withdrawMoney;
        String outputText;

        try {

            withdrawMoney = Double.parseDouble(argument);
            withdrawMoney = Math.abs(withdrawMoney);
            user.setBalance(user.getBalance() - withdrawMoney);

            if (user.getBalance() < 0) {
                outputText = localization.getMessage("withdraw_successful_dept", user.getBalance());
            } else {
                outputText = localization.getMessage("withdraw_successful", user.getBalance());
            }

            return outputText;
        } catch (NumberFormatException e) {
            return localization.getMessage("invalid_amount");
        }
    }
}
