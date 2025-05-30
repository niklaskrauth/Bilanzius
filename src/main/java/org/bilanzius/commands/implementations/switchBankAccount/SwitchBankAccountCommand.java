package org.bilanzius.commands.implementations.switchBankAccount;

import org.bilanzius.commands.CommandController;
import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.commands.Command;
import org.bilanzius.commands.implementations.BankAccountAware;
import org.bilanzius.utils.Localization;

import java.util.List;

public class SwitchBankAccountCommand implements Command, BankAccountAware
{

    private final User user;
    private final BankAccountService bankAccountService;
    private final CommandController commandController;
    private final Localization localization = Localization.getInstance();
    private BankAccount selectedBankAccount;

    public SwitchBankAccountCommand(User user, CommandController commandController)
    {
        this.user = user;
        this.bankAccountService = DatabaseProvider.getBankAccountService();
        this.commandController = commandController;
        this.selectedBankAccount = commandController.getSelectedBankAccount();
    }

    @Override
    public void setSelectedBankAccount(BankAccount bankAccount)
    {
        this.selectedBankAccount = bankAccount;
    }

    @Override
    public String execute(String[] arguments)
    {

        try {

            if (arguments.length != 1) {
                return localization.getMessage("switch_bank_account_usage");
            }

            if (arguments[0].equals(selectedBankAccount.getName())) {
                return localization.getMessage("bank_account_already_selected", arguments[0]);
            }

            List<BankAccount> bankAccountsOfUser;
            BankAccount newSelectedBankAccount;

            try {
                bankAccountsOfUser = bankAccountService.getBankAccountsOfUser(user, 100);
            } catch (
                    DatabaseException e) {
                return localization.getMessage("database_error", e.toString());
            }

            newSelectedBankAccount = bankAccountsOfUser.stream()
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