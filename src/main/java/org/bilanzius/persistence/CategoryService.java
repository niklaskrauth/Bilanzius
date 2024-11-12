package org.bilanzius.persistence;

import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.User;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    void createCategory(Category category);

    List<Category> getCategoriesOfUser(User user, int limit);

    Optional<Category> getCategoriesOfUserByName(User user, String name);

    List<Category> getExceededCategoriesOfUser(User user, int limit);

    void updateCategory(Category category);

}
