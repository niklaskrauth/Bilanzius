package org.bilanzius.services.commands.createBankAccount;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteBankAccountService;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

import java.sql.SQLException;
import java.util.List;

public class CreateBankAccountCommand implements Command {
    private User user;
    private final BankAccountService bankAccountService;
    private final Localization localization = Localization.getInstance();

    public CreateBankAccountCommand(User user, SqlBackend backend) throws SQLException {
        this.user = user;
        this.bankAccountService = SqliteBankAccountService.getInstance(backend);
    }

    @Override
    public String execute(String[] arguments) {

        List<BankAccount> bankAccountsOfUser;

        if (arguments.length != 1) {
            return localization.getMessage("create_bank_account_usage");
        }

        try {
            bankAccountsOfUser = bankAccountService.getBankAccountsOfUser(user, 3);
        }
        catch (DatabaseException e) {
            return localization.getMessage("database_error");
        }

        if (bankAccountsOfUser.size() >= 3) {
            return localization.getMessage("max_number_bank_accounts_reached");
        }

        if (bankAccountsOfUser.stream().anyMatch(bankAccount -> bankAccount.getName().equals(arguments[0]))) {
            return localization.getMessage("bank_account_with_name_already_exists", arguments[0]);
        }

        try {
            bankAccountService.createBankAccount(BankAccount.create(user, arguments[0]));
        } catch (DatabaseException e) {
            return localization.getMessage("database_error");
        }

        return localization.getMessage("bank_account_created", arguments[0]);
    }
}
