package org.bilanzius.commands.implementations.getCategory;

import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.User;
import org.bilanzius.commands.Command;
import org.bilanzius.utils.Localization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GetCategoryCommand implements Command
{

    private User user;
    private final CategoryService categoryService;
    private final Map<GetCategoryCommandArguments, Function<String, String>> commandMap = new HashMap<>();
    private final Localization localization = Localization.getInstance();

    public GetCategoryCommand(User user)
    {
        this.user = user;
        this.categoryService = DatabaseProvider.getCategoryService();

        commandMap.put(GetCategoryCommandArguments.ALL, s -> allCategories());
        commandMap.put(GetCategoryCommandArguments.NAME, this::categoryByName);
        commandMap.put(GetCategoryCommandArguments.EXCEEDED, s -> exceededCategories());
    }

    @Override
    public String execute(String[] arguments)
    {

        GetCategoryCommandArguments argument;
        Function<String, String> command;

        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided", GetCategoryCommandArguments.getAllArguments());
        }

        argument = GetCategoryCommandArguments.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument", GetCategoryCommandArguments.getAllArguments());
        }

        command = commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments.length > 1 ? arguments[1] : null);
        }

        return localization.getMessage("unknown_argument", GetCategoryCommandArguments.getAllArguments());
    }

    private String exceededCategories()
    {

        List<Category> categories;
        try {
            categories =
                    categoryService.getExceededCategoriesOfUser(user, 10).stream().toList();
        } catch (
                DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }

        if (categories.isEmpty()) {
            return localization.getMessage("no_exceeded_categories");
        }

        return categories.stream()
                .map(category -> localization.getMessage("get_category_information",
                        category.getName(), category.getBudget(), category.getAmountSpent()))
                .collect(Collectors.joining("\n"));
    }

    private String categoryByName(String name)
    {

        Category category;
        try {
            category =
                    categoryService.getCategoryOfUserByName(user, name).stream().findFirst().orElse(null);
        } catch (
                DatabaseException e) {
            return localization.getMessage("database_error");
        }

        if (category == null) {
            return localization.getMessage("no_category_with_name", name);
        }

        return localization.getMessage("get_category_information", category.getName(), category.getBudget(),
                category.getAmountSpent());
    }

    private String allCategories()
    {

        List<Category> categories;
        try {
            categories =
                    categoryService.getCategoriesOfUser(user, 10).stream().toList();
        } catch (
                DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }

        if (categories.isEmpty()) {
            return localization.getMessage("no_categories_created");
        }

        return categories.stream()
                .map(category -> localization.getMessage("get_category_information",
                        category.getName(), category.getBudget(), category.getAmountSpent()))
                .collect(Collectors.joining("\n"));
    }
}
