package org.bilanzius.commands.implementations.createCategory;

import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.User;
import org.bilanzius.testharness.persistence.ModelUtils;
import org.bilanzius.testharness.persistence.mock.MockedCategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

class CreateCategoryCommandTest
{

    @Test
    void testCreateCategory()
    {
        var mockedService = new MockedCategoryService();
        var command = new CreateCategoryCommand(mockedService, ModelUtils.existingUser());
        command.execute(new String[]{"-n", "test", "-b", "10"});
        Assertions.assertEquals(1, mockedService.getCreatedCategories());
    }

    @Test
    void testCreateExistingCategory()
    {
        var mockedService = new MockedCategoryService()
        {
            @Override
            public Optional<Category> getCategoryOfUserByName(User user, String name)
            {
                return Optional.of(ModelUtils.category());
            }
        };
        var command = new CreateCategoryCommand(mockedService, ModelUtils.existingUser());
        command.execute(new String[]{"-n", "category", "-b", "10"});
        Assertions.assertEquals(0, mockedService.getCreatedCategories());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "-n test -b", // too few arguments
        "-n test -b 10 12345", // too many arguments
        "-n test -c 10", // unknown argument
        "-b 10 -b 10", // two budgets
        "-n test -b -10", // negative budget
        "-n test -b test" // budget NaN
    })
    void testInvalidArguments(String commandInput)
    {
        var mockedService = new MockedCategoryService();
        var command = new CreateCategoryCommand(mockedService, ModelUtils.existingUser());
        command.execute(commandInput.split(" "));
        System.out.println(commandInput + " CAT: " + mockedService.getCreatedCategories());
        Assertions.assertEquals(0, mockedService.getCreatedCategories());
    }
}
