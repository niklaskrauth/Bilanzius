package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.testharness.persistence.ModelUtils;
import org.bilanzius.testharness.persistence.SqlTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

class SqliteCategoryServiceTest extends SqlTest
{

    @Test
    void testCreateAndDeleteCategory()
    {
        var service = categoryService();
        service.createCategory(Category.create(ModelUtils.existingUser(), "category", BigDecimal.ZERO));

        var category = service.getCategory(1).orElseThrow();
        Assertions.assertEquals("category", category.getName());
        service.deleteCategory(category);
        Assertions.assertTrue(service.getCategory(1).isEmpty());
    }

    @Test
    void testGetCategoryByUser()
    {
        var service = categoryService();
        service.createCategory(Category.create(ModelUtils.existingUser(), "category", BigDecimal.ZERO));

        Assertions.assertEquals(1, service.getCategoriesOfUser(ModelUtils.existingUser(), 100).size());
        Assertions.assertTrue(service.getCategoryOfUserByName(ModelUtils.existingUser(), "category").isPresent());
    }

    @Test
    void testUpdateCategory()
    {
        var service = categoryService();
        service.createCategory(Category.create(ModelUtils.existingUser(), "category", BigDecimal.ZERO));

        var category = service.getCategory(1).orElseThrow();
        category.setName("new_name");
        category.setBudget(BigDecimal.valueOf(100));
        category.setAmountSpent(BigDecimal.valueOf(200));

        service.updateCategory(category);

        var updatedCategory = service.getCategory(1).orElseThrow();
        Assertions.assertEquals("new_name", updatedCategory.getName());

        var exceeded = service.getExceededCategoriesOfUser(ModelUtils.existingUser(), 10);
        Assertions.assertFalse(exceeded.isEmpty());
    }

    @Test
    void testDatabaseExceptionIfNoDatabaseConnection() throws SQLException
    {
        // Simulate connection lost
        var service = categoryService();
        this.sqlBackend.close();

        Assertions.assertThrows(DatabaseException.class, () -> service.deleteCategory(ModelUtils.category()));
        Assertions.assertThrows(DatabaseException.class, () -> service.updateCategory(ModelUtils.category()));
        Assertions.assertThrows(DatabaseException.class, () -> service.createCategory(ModelUtils.category()));
        Assertions.assertThrows(DatabaseException.class, () -> service.getCategoryOfUserByName(ModelUtils.existingUser(), ""));
        Assertions.assertThrows(DatabaseException.class, () -> service.getCategoriesOfUser(ModelUtils.existingUser(), 1));
        Assertions.assertThrows(DatabaseException.class, () -> service.getCategory(1));
        Assertions.assertThrows(DatabaseException.class, () -> service.getExceededCategoriesOfUser(ModelUtils.existingUser(), 1));
    }

    private CategoryService categoryService()
    {
        try
            {
                var backend = requestBackend();
                return new SqliteCategoryService(backend);
            } catch (SQLException ex)
            {
                throw new RuntimeException(ex);
            }
    }
}
