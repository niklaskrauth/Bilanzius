package org.bilanzius.services.commands.createCategory;

import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteCategoryService;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

import java.sql.SQLException;

public class CreateCategoryCommand implements Command  {

    private User user;
    CategoryService categoryService;
    private final Localization localization = Localization.getInstance();

    public CreateCategoryCommand(User user, SqlBackend backend) throws SQLException {
        this.user = user;
        this.categoryService = SqliteCategoryService.getInstance(backend);
    }

    @Override
    public String execute(String[] arguments) {
        if (arguments.length != 4) {
            return localization.getMessage("create_category_usage");
        }

        String name = null;
        Double budget = null;

        for (int i = 0; i < arguments.length; i += 2) {
            CreateCategoryCommandArguments argument = CreateCategoryCommandArguments.fromString(arguments[i]);
            if (argument == null) {
                return localization.getMessage("unknown_argument", CreateCategoryCommandArguments.getAllArguments());
            }
            if (argument == CreateCategoryCommandArguments.NAME) {
                name = arguments[i + 1];
            } else if (argument == CreateCategoryCommandArguments.BUDGET) {
                try {
                    budget = Double.parseDouble(arguments[i + 1]);
                } catch (NumberFormatException e) {
                    return localization.getMessage("invalid_amount");
                }
            }
        }

        if (name != null || budget != null) {
            return createCategory(name, budget);
        }

        return localization.getMessage("create_category_usage");
    }

    private boolean checkIfCategoryExists(String name) {
        Category category = categoryService.getCategoryOfUserByName(user, name).stream().findFirst().orElse(null);
        return category != null && category.getName().equals(name);
    }

    private String createCategory(String name, Double budget) {
        if (checkIfCategoryExists(name)) {
            return localization.getMessage("category_already_exists", name);
        }

        categoryService.createCategory(Category.create(user, name, budget));
        return localization.getMessage("category_created", name, budget);
    }
}
