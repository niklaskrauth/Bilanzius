package org.bilanzius.rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.User;
import org.bilanzius.rest.consumer.CategoryRestConsumer;
import org.bilanzius.rest.dto.CategoryDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.bilanzius.Main.MAX_BANK_ACCOUNTS;
import static org.bilanzius.utils.Requests.readRequestBody;

public class CategoryRestController extends RequestHandler
{

    private final CategoryService categoryService;
    private final Gson gson;

    public CategoryRestController()
    {
        this.gson = new Gson();
        this.categoryService = DatabaseProvider.getCategoryService();
    }

    public void getAllCategories(HttpExchange exchange) throws IOException
    {
        try {
            List<Category> categories;
            List<CategoryDTO> categoryDTOs = new ArrayList<>();

            User user = getUserFromExchange(exchange);

            if (user == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            categories = categoryService.getCategoriesOfUser(user, MAX_BANK_ACCOUNTS);

            if (categories.isEmpty()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            categories.stream().map(
                    category -> new CategoryDTO(category.getName(), category.getBudget(), category.getAmountSpent())
            ).forEach(categoryDTOs::add);

            getRequestHandler(exchange, categoryDTOs);
        } catch (IOException | DatabaseException e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private void modifyCategory(CategoryRestConsumer categoryRestConsumer, HttpExchange exchange) throws IOException
    {
        try {
            CategoryDTO categoryDTO;
            Category category;

            User user = getUserFromExchange(exchange);

            if (user == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            categoryDTO = gson.fromJson(readRequestBody(exchange), CategoryDTO.class);
            Optional<Category> existingCategory = categoryService.getCategoryOfUserByName(user, categoryDTO.name());

            if (existingCategory.isEmpty()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            category = existingCategory.get();
            categoryRestConsumer.accept(user, category, categoryDTO);

        } catch (IOException | JsonSyntaxException | DatabaseException e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    public void createCategory(HttpExchange exchange) throws IOException
    {
        modifyCategory((user, category, categoryDTO) ->
        {
            category = Category.create(user, categoryDTO.name(), categoryDTO.budget());
            categoryService.createCategory(category);
            postRequestHandler(exchange, categoryDTO);
        }, exchange);
    }

    public void updateCategory(HttpExchange exchange) throws IOException
    {
        modifyCategory((user, category, categoryDTO) ->
        {
            category.setBudget(categoryDTO.budget());
            category.setAmountSpent(categoryDTO.amountSpent());
            categoryService.updateCategory(category);
            putRequestHandler(exchange, categoryDTO);
        }, exchange);
    }

    public void deleteCategory(HttpExchange exchange) throws IOException
    {
        modifyCategory((user, category, categoryDTO) ->
        {
            categoryService.deleteCategory(category);
            deleteRequestHandler(exchange, categoryDTO);
        }, exchange);
    }

    private User getUserFromExchange(HttpExchange exchange) throws IOException
    {
        handle(exchange);
        return (User) exchange.getAttribute("user");
    }
}