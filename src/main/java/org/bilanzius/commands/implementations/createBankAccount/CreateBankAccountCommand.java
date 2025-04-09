package org.bilanzius.commands.implementations.createBankAccount;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.commands.Command;
import org.bilanzius.utils.Localization;

import java.util.List;

public class CreateBankAccountCommand implements Command
{
    private User user;
    private final BankAccountService bankAccountService;
    private final Localization localization = Localization.getInstance();

    public CreateBankAccountCommand(User user)
    {
        this.user = user;
        this.bankAccountService = DatabaseProvider.getBankAccountService();
    }

    @Override
    public String execute(String[] arguments)
    {

        List<BankAccount> bankAccountsOfUser;
        String bankAccountName;

        if (arguments.length != 1) {
            return localization.getMessage("create_bank_account_usage");
        }

        bankAccountName =
                arguments[0];

        if (bankAccountName.length() > 20) {
            return localization.getMessage("bank_account_name_too_long");
        }

        if (bankAccountName.isEmpty()) {
            return localization.getMessage("bank_account_name_too_short");
        }

        try {
            bankAccountsOfUser = bankAccountService.getBankAccountsOfUser(user, 3);
        } catch (
                DatabaseException e) {
            return localization.getMessage("database_error");
        }

        if (bankAccountsOfUser.size() >= 3) {
            return localization.getMessage("max_number_bank_accounts_reached");
        }

        if (bankAccountsOfUser.stream().anyMatch(bankAccount -> bankAccount.getName().equals(bankAccountName))) {
            return localization.getMessage("bank_account_with_name_already_exists", arguments[0]);
        }

        try {
            bankAccountService.createBankAccount(BankAccount.create(user, bankAccountName));
        } catch (
                DatabaseException e) {
            return localization.getMessage("database_error");
        }

        return localization.getMessage("bank_account_created", bankAccountName);
    }
}
