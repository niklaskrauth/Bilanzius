package org.bilanzius.commands.implementations.createCategory;

import org.bilanzius.commands.Command;
import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.User;
import org.bilanzius.utils.Localization;

import java.math.BigDecimal;

public class CreateCategoryCommand implements Command
{
    private final User user;
    private final CategoryService categoryService;
    private final Localization localization = Localization.getInstance();
    private String name = null;
    private BigDecimal budget = null;

    public CreateCategoryCommand(CategoryService categoryService, User user)
    {
        this.categoryService = categoryService;
        this.user = user;
    }

    @Override
    public String execute(String[] arguments)
    {
        if (arguments.length != 4) {
            return localization.getMessage("create_category_usage");
        }

        try {
            this.parseArguments(arguments);
        } catch (RuntimeException ex) {
            return ex.getMessage();
        }

        if (this.name != null && this.budget != null) {
            return createCategory(this.name, this.budget);
        }

        return localization.getMessage("create_category_usage");
    }

    private void parseArguments(String[] arguments)
    {
        for (int i = 0; i < arguments.length; i += 2) {
            CreateCategoryCommandArguments argument = CreateCategoryCommandArguments.fromString(arguments[i]);
            if (argument == null) {
                throw new RuntimeException(localization.getMessage("unknown_argument", CreateCategoryCommandArguments.getAllArguments()));
            }

            handleArgument(argument, arguments[i + 1]);
        }
    }

    private void handleArgument(CreateCategoryCommandArguments argument, String value)
    {
        if (argument == CreateCategoryCommandArguments.NAME) {
            this.name = value;
            return;
        }

        if (argument == CreateCategoryCommandArguments.BUDGET) {
            handleBudgetArgument(value);
        }
    }

    private void handleBudgetArgument(String value)
    {
        try {
            this.budget = BigDecimal.valueOf(Double.parseDouble(value));
            if (this.budget.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException(localization.getMessage("invalid_amount"));
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(localization.getMessage("invalid_amount"));
        }
    }

    private boolean checkIfCategoryExists(String name)
    {
        Category category =
            categoryService.getCategoryOfUserByName(user, name).stream().findFirst().orElse(null);
        return category != null && category.getName().equals(name);
    }

    private String createCategory(String name, BigDecimal budget)
    {
        if (checkIfCategoryExists(name)) {
            return localization.getMessage("category_already_exists", name);
        }

        categoryService.createCategory(Category.create(user, name, budget));
        return localization.getMessage("category_created", name, budget);
    }
}
