package org.bilanzius.rest.consumer;

import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.User;
import org.bilanzius.rest.dto.CategoryDTO;

import java.io.IOException;

@FunctionalInterface
public interface CategoryRestConsumer
{

    void accept(User user, Category category, CategoryDTO categoryDTO) throws IOException;

}
