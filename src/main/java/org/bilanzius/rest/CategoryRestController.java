package org.bilanzius.rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.User;
import org.bilanzius.rest.dto.CategoryDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.bilanzius.Main.MAX_BANK_ACCOUNTS;
import static org.bilanzius.utils.Requests.readRequestBody;

public class CategoryRestController extends RequestHandler {

    private final CategoryService categoryService;
    private final Gson gson = new Gson();

    public CategoryRestController() {
        this.categoryService = DatabaseProvider.getCategoryService();
    }

    public void getAllCategories(HttpExchange exchange) throws IOException {
        try {
            handle(exchange);
            User user = (User) exchange.getAttribute("user");
            if (user == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            List<Category> categories = categoryService.getCategoriesOfUser(user, MAX_BANK_ACCOUNTS);
            if (categories.isEmpty()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            List<CategoryDTO> categoryDTOs = new ArrayList<>();
            categories.forEach(category -> categoryDTOs.add(new CategoryDTO(category.getName(), category.getBudget(), category.getAmountSpent())));
            getRequestHandler(exchange, categoryDTOs);
        } catch (IOException | DatabaseException e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    public void createCategory(HttpExchange exchange) throws IOException {
        try {
            handle(exchange);
            User user = (User) exchange.getAttribute("user");
            if (user == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            CategoryDTO categoryDTO = gson.fromJson(readRequestBody(exchange), CategoryDTO.class);
            if (categoryService.getCategoryOfUserByName(user, categoryDTO.name()).isPresent()) {
                exchange.sendResponseHeaders(409, -1);
                return;
            }

            Category category = Category.create(user, categoryDTO.name(), categoryDTO.budget());
            categoryService.createCategory(category);
            postRequestHandler(exchange, categoryDTO);
        } catch (IOException | JsonSyntaxException | DatabaseException e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    public void updateCategory(HttpExchange exchange) throws IOException {
        try {
            handle(exchange);
            User user = (User) exchange.getAttribute("user");
            if (user == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            CategoryDTO categoryDTO = gson.fromJson(readRequestBody(exchange), CategoryDTO.class);
            Optional<Category> existingCategory = categoryService.getCategoryOfUserByName(user, categoryDTO.name());
            if (existingCategory.isEmpty()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            Category category = existingCategory.get();
            category.setBudget(categoryDTO.budget());
            category.setAmountSpent(categoryDTO.amountSpent());
            categoryService.updateCategory(category);
            putRequestHandler(exchange, categoryDTO);
        } catch (IOException | JsonSyntaxException | DatabaseException e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    public void deleteCategory(HttpExchange exchange) throws IOException {
        try {
            handle(exchange);
            User user = (User) exchange.getAttribute("user");
            if (user == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            CategoryDTO categoryDTO = gson.fromJson(readRequestBody(exchange), CategoryDTO.class);
            Optional<Category> existingCategory = categoryService.getCategoryOfUserByName(user, categoryDTO.name());
            if (existingCategory.isEmpty()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            categoryService.deleteCategory(existingCategory.get());
            deleteRequestHandler(exchange, categoryDTO);
        } catch (IOException | JsonSyntaxException | DatabaseException e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }
}