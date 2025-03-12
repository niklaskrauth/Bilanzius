package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.testharness.persistence.ModelUtils;
import org.bilanzius.testharness.persistence.SqlTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

public class SqliteCategoryServiceTest extends SqlTest {

    @Test
    void testCreateCategory() {
        var service = categoryService();
        service.createCategory(Category.create(ModelUtils.existingUser(), "category", BigDecimal.ZERO));

        var category = service.getCategory(1).orElseThrow();
        Assertions.assertEquals("category", category.getName());
    }

    private CategoryService categoryService() {
        try {
            var backend = requestBackend();
            return new SqliteCategoryService(backend);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
