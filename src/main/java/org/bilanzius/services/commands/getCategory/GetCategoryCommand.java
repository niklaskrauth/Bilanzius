package org.bilanzius.services.commands.getCategory;

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
import java.util.function.Function;
import java.util.stream.Collectors;

public class GetCategoryCommand implements Command {
    private User user;
    CategoryService categoryService;
    private final Map<GetCategoryCommandArguments, Function<String, String>> commandMap;
    private final Localization localization = Localization.getInstance();

    public GetCategoryCommand(User user, SqlBackend backend) throws SQLException {
        this.user = user;
        this.categoryService = SqliteCategoryService.getInstance(backend);

        commandMap = new HashMap<>();
        commandMap.put(GetCategoryCommandArguments.ALL, s -> allCategories());
        commandMap.put(GetCategoryCommandArguments.NAME, this::categoryByName);
        commandMap.put(GetCategoryCommandArguments.EXCEEDED, s -> exceededCategories());
    }

    @Override
    public String execute(String[] arguments) {
        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided", GetCategoryCommandArguments.getAllArguments());
        }
        GetCategoryCommandArguments argument = GetCategoryCommandArguments.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument", GetCategoryCommandArguments.getAllArguments());
        }

        Function<String, String> command = commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments.length > 1 ? arguments[1] : null);
        }

        return localization.getMessage("unknown_argument", GetCategoryCommandArguments.getAllArguments());
    }

    private String exceededCategories() {
        List<Category> categories = categoryService.getExceededCategoriesOfUser(user, 10).stream().toList();
        if (categories.isEmpty()) {
            return localization.getMessage("no_exceeded_categories");
        }
        return categories.stream()
                .map(category -> localization.getMessage("get_category_information",
                        category.getName(), category.getBudget(), category.getAmountSpent()))
                .collect(Collectors.joining("\n"));
    }

    private String categoryByName(String name) {
        Category category = categoryService.getCategoryOfUserByName(user, name).stream().findFirst().orElse(null);
        if (category == null) {
            return localization.getMessage("no_category_with_name", name);
        }
        return localization.getMessage("get_category_information", category.getName(), category.getBudget(),
                category.getAmountSpent());
    }

    private String allCategories() {
        List<Category> categories = categoryService.getCategoriesOfUser(user, 10).stream().toList();
        if (categories.isEmpty()) {
            return localization.getMessage("no_categories_created");
        }
        return categories.stream()
                .map(category -> localization.getMessage("get_category_information",
                        category.getName(), category.getBudget(), category.getAmountSpent()))
                .collect(Collectors.joining("\n"));
    }
}
