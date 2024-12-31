package org.bilanzius.services.commands.createBankAccount;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteBankAccountService;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

import java.sql.SQLException;

public class CreateBankAccountCommand implements Command {
    private User user;
    private final BankAccountService bankAccountService;
    private final Localization localization = Localization.getInstance();

    public CreateBankAccountCommand(User user, SqlBackend backend) throws SQLException {
        this.user = user;
        this.bankAccountService = SqliteBankAccountService.getInstance(backend);
    }

    public boolean checkCurrentBankAccountCount() {
        return bankAccountService.getBankAccountsOfUser(user, 5).size() < 3;
    }

    @Override
    public String execute(String[] arguments) {
        if (arguments.length != 1) {
            return localization.getMessage("create_bank_account_usage");
        }
        if (!checkCurrentBankAccountCount()) {
            return localization.getMessage("max_number_bank_accounts_reached");
        }
        bankAccountService.createBankAccount(BankAccount.create(user, arguments[0]));
        return localization.getMessage("bank_account_created", arguments[0]);
    }
}
