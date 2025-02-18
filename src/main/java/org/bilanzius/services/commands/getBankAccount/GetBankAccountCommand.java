package org.bilanzius.services.commands.getBankAccount;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteBankAccountService;
import org.bilanzius.services.Command;
import org.bilanzius.services.commands.getCategory.GetCategoryCommandArguments;
import org.bilanzius.utils.Localization;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GetBankAccountCommand implements Command {
    private User user;
    private final BankAccountService bankAccountService;
    private final Map<GetBankAccountCommandArguments, Function<String, String>> commandMap;
    private final Localization localization = Localization.getInstance();

    public GetBankAccountCommand(User user, SqlBackend backend) throws SQLException {
        this.user = user;
        this.bankAccountService = SqliteBankAccountService.getInstance(backend);

        commandMap = new HashMap<>();
        commandMap.put(GetBankAccountCommandArguments.ALL, s -> allBankAccounts());
        commandMap.put(GetBankAccountCommandArguments.NAME, this::bankAccountByName);
    }

    @Override
    public String execute(String[] arguments) {
        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided", GetBankAccountCommandArguments.getAllArguments());
        }
        GetBankAccountCommandArguments argument = GetBankAccountCommandArguments.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument", GetBankAccountCommandArguments.getAllArguments());
        }

        Function<String, String> command = commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments.length > 1 ? arguments[1] : null);
        }

        return localization.getMessage("unknown_argument", GetCategoryCommandArguments.getAllArguments());
    }

    private String bankAccountByName(String name) {
        BankAccount bankAccount = bankAccountService.getBankAccountsOfUserByName(user, name).stream().findFirst().orElse(null);
        if (bankAccount == null) {
            return localization.getMessage("no_bank_account_with_name", name);
        }
        return localization.getMessage("get_bank_account_information", bankAccount.getName(), bankAccount.getBalance());
    }

    private String allBankAccounts() {
        List<BankAccount> bankAccounts = bankAccountService.getBankAccountsOfUser(user, 10).stream().toList();
        if (bankAccounts.isEmpty()) {
            return localization.getMessage("no_bank_accounts_yet");
        }
        return bankAccounts.stream()
                .map(bankAccount -> localization.getMessage("get_bank_account_information",
                        bankAccount.getName(), bankAccount.getBalance())).collect(Collectors.joining("\n"));
    }
}
