package org.bilanzius.persistence.sql.test;

import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.TransactionService;
import org.bilanzius.persistence.UserDatabaseService;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.Transaction;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteCategoryService;
import org.bilanzius.persistence.sql.SqliteTransactionService;
import org.bilanzius.persistence.sql.SqliteUserDatabaseService;
import org.bilanzius.utils.HashedPassword;

public class Test {

    public static void main(String[] args) throws Exception {
        // Connect to sqllite database
        var backend = new SqlBackend();
        backend.connect();

        // Setup
        UserDatabaseService userService = new SqliteUserDatabaseService(backend);
        TransactionService transactionService = new SqliteTransactionService(backend);
        CategoryService categoryService = new SqliteCategoryService(backend);

        // Create a new user with name "test2" and password "passwort123"
        userService.createUser(User.createUser("test2", HashedPassword.fromPlainText("passwort123")));

        // Find user with credentials
        var user = userService
                .findUserWithCredentials("test2", HashedPassword.fromPlainText("passwort123"))
                .orElseThrow();

        // create transaction
        transactionService.saveTransaction(Transaction.create(user, 5.00, "5€"));

        // Update password
        user.setHashedPassword(HashedPassword.fromPlainText("kuchen123"));
        userService.updateUser(user);

        // Create category
        categoryService.createCategory(Category.create(user, "test", 5.00, 0.00));

        // Get category
        var category = categoryService.getCategoriesOfUser(user, 1).getFirst();

        // Update category
        category.setName("Test");
        category.setBudget(10.00);
        category.setAmountSpent(5.00);
        categoryService.updateCategory(category);

        // Get category by name
        var categoryTest = categoryService.getCategoriesOfUserByName(user, "Test").orElseThrow();
        categoryTest.toString();

        // Get exceeded categories
        categoryTest.setAmountSpent(15.00);
        categoryService.updateCategory(categoryTest);
        var exceededCategories = categoryService.getExceededCategoriesOfUser(user, 1).getFirst();
        exceededCategories.toString();
    }
}
