package org.bilanzius;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.UserService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.utils.Localization;

import java.util.Optional;
import java.util.Scanner;

import static org.bilanzius.utils.HashedPassword.fromPlainText;

public class SignUp {

    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final Localization localization = Localization.getInstance();

    public SignUp(UserService userService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    public User waitUntilLoggedIn (Scanner input) {

        while (true) {
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("Username:");
            String stringInput = input.nextLine();

            Optional<User> userOptional = userService.findUserWithName(stringInput);
            if (userOptional.isPresent()) {

                User user = userOptional.get();
                System.out.println("Password:");
                stringInput = input.nextLine();

                if (fromPlainText(stringInput).equals(user.getHashedPassword())) {

                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.println(localization.getMessage("greeting_user", user.getUsername()));

                    return user;

                } else {
                    System.out.println(localization.getMessage("wrongPassword"));
                }

            } else {
                System.out.println(localization.getMessage("wrongUsername"));
            }
        }
    }

    public BankAccount waitUntilBankAccountSelect(Scanner input, User user) {
        System.out.println("----------------------------------------------------------------------------------");
        if (!bankAccountService.getBankAccountsOfUser(user, 1).isEmpty()) {
            System.out.println(localization.getMessage("selectBankAccount"));
            bankAccountService.getBankAccountsOfUser(user, 10).forEach(account -> System.out.println(account.getName()));
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println(localization.getMessage("bankAccountName"));
            while (true) {
                String stringInput = input.nextLine();
                BankAccount account = bankAccountService.getBankAccountsOfUserByName(user, stringInput).orElse(null);
                if (account != null) {
                    return account;
                }
                System.out.println(localization.getMessage("wrongBankAccountName"));
            }
        }
        System.out.println(localization.getMessage("noBankAccountsYet"));
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println(localization.getMessage("bankAccountName"));
        String stringInput = input.nextLine();
        BankAccount bankAccount = BankAccount.create(user, stringInput);
        bankAccountService.createBankAccount(bankAccount);
        System.out.println(localization.getMessage("bankAccountCreated", stringInput));
        return bankAccount;
    }
}
