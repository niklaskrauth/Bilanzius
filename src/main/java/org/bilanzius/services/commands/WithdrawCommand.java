package org.bilanzius.services.commands;

import org.bilanzius.User;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class WithdrawCommand implements Command {

    private User user;
    private final Map<WithdrawCommandArgument, Function<String, String>> commandMap;


    public WithdrawCommand(User user) {
        this.user = user;

        commandMap = new HashMap<>();
        commandMap.put(WithdrawCommandArgument.WITHDRAW, this::withdrawMoney);

    }

    @Override
    public String execute(String[] arguments) {

        if (arguments == null || arguments.length == 0) {
            return Localization.getInstance().getMessage("no_arguments_provided",
                    WithdrawCommandArgument.getAllArguments());
        }

        WithdrawCommandArgument argument = WithdrawCommandArgument.fromString(arguments[0]);
        if (argument == null) {
            return Localization.getInstance().getMessage("unknown_argument",
                    WithdrawCommandArgument.getAllArguments());
        }

        Function<String, String> command = commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments.length > 1 ? arguments[1] : null);
        }

        return Localization.getInstance().getMessage("unknown_argument", WithdrawCommandArgument.getAllArguments());
    }

    private String withdrawMoney(String argument) {

        double withdrawMoney;
        String outputText;

        try {

            withdrawMoney = Double.parseDouble(argument);
            user.setBalance(user.getBalance() - withdrawMoney);

            if (user.getBalance() < 0) {
                outputText = Localization.getInstance().getMessage("withdraw_successful_dept", user.getBalance());
            } else {
                outputText = Localization.getInstance().getMessage("withdraw_successful", user.getBalance());
            }

            return outputText;
        } catch (NumberFormatException e) {
            return Localization.getInstance().getMessage("invalid_amount");
        }
    }
}
