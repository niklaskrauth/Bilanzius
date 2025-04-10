package org.bilanzius.testharness.persistence.mock;

import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.User;

import java.util.List;
import java.util.Optional;

public class MockedCategoryService implements CategoryService
{

    private int createdCategories;

    @Override
    public void createCategory(Category category)
    {
        createdCategories++;
    }

    @Override
    public Optional<Category> getCategory(long id)
    {
        return Optional.empty();
    }

    @Override
    public List<Category> getCategoriesOfUser(User user, int limit)
    {
        return List.of();
    }

    @Override
    public Optional<Category> getCategoryOfUserByName(User user, String name)
    {
        return Optional.empty();
    }

    @Override
    public List<Category> getExceededCategoriesOfUser(User user, int limit)
    {
        return List.of();
    }

    @Override
    public void updateCategory(Category category)
    {

    }

    @Override
    public void deleteCategory(Category category)
    {

    }

    public int getCreatedCategories()
    {
        return createdCategories;
    }
}
