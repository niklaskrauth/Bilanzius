package org.bilanzius.services.commands.renameBankAccount;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteBankAccountService;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

import java.sql.SQLException;
import java.util.List;

public class RenameBankAccountCommand implements Command
{
    private User user;
    private final BankAccountService bankAccountService;
    private final Localization localization = Localization.getInstance();

    public RenameBankAccountCommand(User user)
    {
        this.user = user;
        this.bankAccountService = DatabaseProvider.getBankAccountService();
    }

    @Override
    public String execute(String[] arguments)
    {

        List<BankAccount> bankAccountsOfUser;

        if (arguments.length != 2) {
            return localization.getMessage("rename_bank_account_usage");
        }

        try {
            bankAccountsOfUser = bankAccountService.getBankAccountsOfUser(user, 100);
        } catch (
                DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }

        if (bankAccountsOfUser.stream().noneMatch(bankAccount -> bankAccount.getName().equals(arguments[0]))) {
            return localization.getMessage("no_bank_account_with_name", arguments[0]);
        }

        if (bankAccountsOfUser.stream().anyMatch(bankAccount -> bankAccount.getName().equals(arguments[1]))) {
            return localization.getMessage("bank_account_with_name_already_exists", arguments[1]);
        }

        BankAccount renamedBankAccount = bankAccountsOfUser.stream().filter(bankAccount ->
                bankAccount.getName().equals(arguments[0])).findFirst().orElse(null);
        assert renamedBankAccount != null;
        renamedBankAccount.setName(arguments[1]);

        try {
            bankAccountService.updateBankAccount(renamedBankAccount);
        } catch (
                DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }

        return localization.getMessage("bank_account_renamed", arguments[1]);
    }
}
