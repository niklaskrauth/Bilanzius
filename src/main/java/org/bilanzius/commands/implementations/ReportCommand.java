package org.bilanzius.commands.implementations;

import org.bilanzius.persistence.models.User;
import org.bilanzius.report.html.HtmlReport;
import org.bilanzius.commands.Command;
import org.bilanzius.utils.Localization;

public class ReportCommand implements Command
{

    private final User user;

    public ReportCommand(User user)
    {
        this.user = user;
    }

    @Override
    public String execute(String[] arguments)
    {
        var reporter =
                HtmlReport.reporter();
        reporter.create(this.user);
        return Localization.getInstance().getMessage("report_result", reporter.getFile().getAbsolutePath());
    }
}
