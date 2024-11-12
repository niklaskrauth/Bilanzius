package org.bilanzius.services.commands;

import org.bilanzius.User;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GetCategoryCommand implements Command {

    private User user;
    private final Map<GetCategoryCommandArguments, Function<String, String>> commandMap;
    private final Localization localization = Localization.getInstance();

    public GetCategoryCommand(User user) {
        this.user = user;

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

        return localization.getMessage("unknown_argument", DepositCommandArguments.getAllArguments());
    }

    private String exceededCategories() {
        //TODO implement
//        CategoryService categoryService = new SqliteCategoryService(new SqlBackend());
//        categoryService.getExceededCategoriesOfUser(user, 10);
        return "";
    }

    private String categoryByName(String name) {
        //TODO implement
//        CategoryService categoryService = new SqliteCategoryService(new SqlBackend());
//        categoryService.getCategoriesOfUserByName(user, name);
        return "";
    }

    private String allCategories() {
        //TODO implement
//        CategoryService categoryService = new SqliteCategoryService(new SqlBackend());
//        categoryService.getCategoriesOfUser(user, 10);
        return "";
    }
}
