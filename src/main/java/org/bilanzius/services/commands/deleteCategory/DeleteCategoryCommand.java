package org.bilanzius.services.commands.deleteCategory;

import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteCategoryService;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

public class DeleteCategoryCommand implements Command {

    private User user;
    CategoryService categoryService;
    private final Map<DeleteCategoryCommandArguments, Function<String, String>> commandMap;
    private final Localization localization = Localization.getInstance();
    private final Scanner scanner;

    public DeleteCategoryCommand(User user, SqlBackend backend) throws SQLException {
        this.user = user;
        this.categoryService = SqliteCategoryService.getInstance(backend);
        this.scanner = new Scanner(System.in);

        commandMap = new HashMap<>();
        commandMap.put(DeleteCategoryCommandArguments.ALL, s -> deleteAllCategories());
        commandMap.put(DeleteCategoryCommandArguments.NAME, this::deleteCategoryByName);
    }

    @Override
    public String execute(String[] arguments) {
        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided", DeleteCategoryCommandArguments.getAllArguments());
        }

        DeleteCategoryCommandArguments argument = DeleteCategoryCommandArguments.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument", DeleteCategoryCommandArguments.getAllArguments());
        }

        Function<String, String> command = commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments.length > 1 ? arguments[1] : null);
        }

        return localization.getMessage("unknown_argument", DeleteCategoryCommandArguments.getAllArguments());
    }

    private String deleteCategoryByName(String name) {
        Category category = categoryService.getCategoryOfUserByName(user, name).stream().findFirst().orElse(null);
        if (category == null) {
            return localization.getMessage("no_category_with_name", name);
        }
        if (validateDeleteAction(localization.getMessage("ask_for_deletion_category", category.getName()))) {
            return localization.getMessage("no_categories_deleted");
        }
        categoryService.deleteCategory(category);
        return localization.getMessage("category_deleted", category.getName());
    }

    private String deleteAllCategories() {
        List<Category> categories = categoryService.getCategoriesOfUser(user, 100).stream().toList();
        if (categories.isEmpty()) {
            return localization.getMessage("no_categories_created");
        }
        if (validateDeleteAction(localization.getMessage("ask_for_deletion_of_all_categories"))) {
            return localization.getMessage("no_categories_deleted");
        }
        for (Category category : categories) {
            categoryService.deleteCategory(category);
        }
        return localization.getMessage("all_categories_deleted");
    }

    private boolean validateDeleteAction(String message){
        System.out.println(message + " (yes/no): ");
        String response = this.scanner.nextLine().trim().toLowerCase();
        return !response.equals("yes") && !response.equals("y");
    }
}
