package org.bilanzius.persistence.sql.test;

import org.bilanzius.persistence.*;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.Transaction;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.*;
import org.bilanzius.utils.HashedPassword;

import java.math.BigDecimal;

public class Test {

    public static void main(String[] args) throws Exception {
        // Connect to sqllite database
        var backend = new SqlBackend();
        backend.connect();

        // Setup
        UserService userService = DatabaseProvider.getUserService();
        TransactionService transactionService = DatabaseProvider.getTransactionService();
        CategoryService categoryService = DatabaseProvider.getCategoryService();
        BankAccountService bankAccountService = DatabaseProvider.getBankAccountService();

        // Create a new user with name "test2" and password "passwort123"
        userService.createUser(User.createUser("test2", HashedPassword.fromPlainText("passwort123")));

        // Find user with credentials
        var user = userService
                .findUserWithCredentials("test2", HashedPassword.fromPlainText("passwort123"))
                .orElseThrow();


        // Create bank account
        bankAccountService.createBankAccount(BankAccount.create(user, "testBankAccount"));
        bankAccountService.getBankAccountsOfUser(user, 1).forEach(System.out::println);
        BankAccount bankAccount = bankAccountService.getBankAccountOfUserByName(user, "testBankAccount").orElseThrow();
        // Update bank account balance
        bankAccountService.updateBankAccountBalance(bankAccount, 5.00);
        // change bank account name
        bankAccount.setName("changedBankAccount");
        bankAccountService.updateBankAccount(bankAccount);
        System.out.println(bankAccountService.getBankAccountOfUserByName(user, "changedBankAccount").orElseThrow());

        // Create category
        categoryService.createCategory(Category.create(user, "test", BigDecimal.valueOf(5.00)));

        // Get category
        var category = categoryService.getCategoriesOfUser(user, 1).getFirst();

        // Update category
        category.setName("Test");
        category.setBudget(BigDecimal.valueOf(10.00));
        category.setAmountSpent(BigDecimal.valueOf(5.00));
        categoryService.updateCategory(category);

        // Get category by name
        var categoryTest = categoryService.getCategoryOfUserByName(user, "Test").stream().findFirst().orElse(null);
        assert categoryTest != null;
        System.out.println(categoryTest);

        // Get exceeded categories
        categoryTest.setAmountSpent(BigDecimal.valueOf(15.00));
        categoryService.updateCategory(categoryTest);
        var exceededCategories = categoryService.getExceededCategoriesOfUser(user, 1).getFirst();
        System.out.println(exceededCategories.toString());

        // create transaction
        //transactionService.saveTransaction(Transaction.create(user, bankAccount, categoryTest, BigDecimal.valueOf(-5.00), "-5€"));
        //transactionService.saveTransaction(Transaction.create(user, bankAccount, BigDecimal.valueOf(10.00), "10€"));

        // Update password
        user.setHashedPassword(HashedPassword.fromPlainText("kuchen123"));
        userService.updateUser(user);

    }
}
