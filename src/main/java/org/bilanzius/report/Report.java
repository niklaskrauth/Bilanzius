package org.bilanzius.report;

import org.bilanzius.persistence.models.User;

public interface Report {

    void create(User user);
}
