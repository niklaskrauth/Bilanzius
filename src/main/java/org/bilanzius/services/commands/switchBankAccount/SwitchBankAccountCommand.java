package org.bilanzius.services.commands.switchBankAccount;

import org.bilanzius.commandController.CommandController;
import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteBankAccountService;
import org.bilanzius.services.Command;
import org.bilanzius.services.commands.BankAccountAware;
import org.bilanzius.utils.Localization;

import java.sql.SQLException;
import java.util.List;

public class SwitchBankAccountCommand implements Command, BankAccountAware {
    private final User user;
    private final BankAccountService bankAccountService;
    private final CommandController commandController;
    private final Localization localization = Localization.getInstance();
    private BankAccount selectedBankAccount;

    public SwitchBankAccountCommand(User user, SqlBackend backend, CommandController commandController) throws SQLException {
        this.user = user;
        this.bankAccountService = SqliteBankAccountService.getInstance(backend);
        this.commandController = commandController;
        this.selectedBankAccount = commandController.getSelectedBankAccount();
    }
    @Override
    public void setSelectedBankAccount(BankAccount bankAccount) {
        this.selectedBankAccount = bankAccount;
    }

    @Override
    public String execute(String[] arguments) {
        try {
            if (arguments.length != 1) {
                return localization.getMessage("switch_bank_account_usage");
            }
            if (arguments[0].equals(selectedBankAccount.getName())) {
                return localization.getMessage("bank_account_already_selected", arguments[0]);
            }
            List<BankAccount> bankAccountsOfUser = bankAccountService.getBankAccountsOfUser(user, 100);
            BankAccount newSelectedBankAccount = bankAccountsOfUser.stream()
                    .filter(bankAccount -> bankAccount.getName().equals(arguments[0]))
                    .findFirst()
                    .orElse(null);
            if (newSelectedBankAccount == null) {
                return localization.getMessage("no_bank_account_with_name", arguments[0]);
            }
            commandController.setSelectedBankAccount(newSelectedBankAccount);
            return localization.getMessage("bank_account_switched", arguments[0]);
        } catch (Exception e) {
            return localization.getMessage("error_switching_bank_account", e.getMessage());
        }
    }
}