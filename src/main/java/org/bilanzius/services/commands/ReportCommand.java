package org.bilanzius.services.commands;

import org.bilanzius.persistence.models.User;
import org.bilanzius.report.html.HtmlReport;
import org.bilanzius.services.Command;

public class ReportCommand implements Command {

    private final User user;

    public ReportCommand(User user) {
        this.user = user;
    }

    @Override
    public String execute(String[] arguments) {
        var reporter = HtmlReport.reporter();
        reporter.create(this.user);
        return "OK";
    }
}
