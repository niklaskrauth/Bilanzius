package org.bilanzius.services.commands;

import org.bilanzius.User;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

public class CreateCategoryCommand implements Command  {

    private User user;
    private final Localization localization = Localization.getInstance();

    public CreateCategoryCommand(User user) {
        this.user = user;
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
        // TODO implement check if category exists
        //        CategoryService categoryService = new SqliteCategoryService(new SqlBackend());
        //        categoryService.getCategoriesOfUserByName(user, name);
        return false;
    }

    private String createCategory(String name, Double budget) {
        if (checkIfCategoryExists(name)) {
            return localization.getMessage("category_already_exists", name);
        }

//        CategoryService categoryService = new SqliteCategoryService(new SqlBackend());
//        categoryService.createCategory(Category.create(user, name, budget));
        return localization.getMessage("category_created", name, budget);
    }
}
