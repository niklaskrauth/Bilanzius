package org.bilanzius.services.commands.deleteCategory;

import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.User;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.bilanzius.utils.ValidateDelete.validateDeleteAction;

public class DeleteCategoryCommand implements Command
{

    private User user;
    private final CategoryService categoryService;
    private final Map<DeleteCategoryCommandArguments, Function<String, String>> commandMap;
    private final Localization localization = Localization.getInstance();

    public DeleteCategoryCommand(User user)
    {
        this.user = user;
        this.categoryService = DatabaseProvider.getCategoryService();

        commandMap =
                new HashMap<>();
        commandMap.put(DeleteCategoryCommandArguments.ALL, s -> deleteAllCategories());
        commandMap.put(DeleteCategoryCommandArguments.NAME, this::deleteCategoryByName);
    }

    @Override
    public String execute(String[] arguments)
    {
        DeleteCategoryCommandArguments argument;
        Function<String,
                String> command;

        if (arguments == null || arguments.length == 0)
        {
            return localization.getMessage("no_arguments_provided", DeleteCategoryCommandArguments.getAllArguments());
        }

        argument =
                DeleteCategoryCommandArguments.fromString(arguments[0]);
        if (argument == null)
        {
            return localization.getMessage("unknown_argument", DeleteCategoryCommandArguments.getAllArguments());
        }

        command =
                commandMap.get(argument);
        if (command != null)
        {
            return command.apply(arguments.length > 1 ? arguments[1] : null);
        }

        return localization.getMessage("unknown_argument", DeleteCategoryCommandArguments.getAllArguments());
    }

    private String deleteCategoryByName(String name)
    {

        Category category;

        try
        {
            category =
                    categoryService.getCategoryOfUserByName(user, name).stream().findFirst().orElse(null);
        } catch (
                DatabaseException e)
        {
            return localization.getMessage("database_error", e.toString());
        }

        if (category == null)
        {
            return localization.getMessage("no_category_with_name", name);
        }

        if (validateDeleteAction(localization.getMessage("ask_for_deletion_category", category.getName())))
        {
            return localization.getMessage("no_categories_deleted");
        }

        try
        {
            categoryService.deleteCategory(category);
        } catch (
                DatabaseException e)
        {
            return localization.getMessage("database_error", e.toString());
        }

        return localization.getMessage("category_deleted", category.getName());
    }

    private String deleteAllCategories()
    {

        List<Category> categories;

        try
        {
            categories =
                    categoryService.getCategoriesOfUser(user, 100).stream().toList();
        } catch (
                DatabaseException e)
        {
            return localization.getMessage("database_error", e.toString());
        }

        if (categories.isEmpty())
        {
            return localization.getMessage("no_categories_created");
        }

        if (validateDeleteAction(localization.getMessage("ask_for_deletion_of_all_categories")))
        {
            return localization.getMessage("no_categories_deleted");
        }

        for (Category category : categories)
        {

            try
            {
                categoryService.deleteCategory(category);
            } catch (
                    DatabaseException e)
            {
                return localization.getMessage("database_error", e.toString());
            }

        }

        return localization.getMessage("all_categories_deleted");
    }


}
