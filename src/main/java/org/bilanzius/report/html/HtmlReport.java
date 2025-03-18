package org.bilanzius.report.html;

import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.report.Report;
import org.bilanzius.utils.Localization;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class HtmlReport implements Report {

    public static HtmlReport reporter() {
        return new HtmlReport(new File("report.html"));
    }

    private final File file;

    private HtmlReport(File file) {
        this.file = file;
    }

    @Override
    public void create(User user) {
        var report = HtmlGenerator.create()
                .htmlFromResources("report.html")
                .replace("user", PlainText.text(user.getUsername()));

        var bankAccounts = DatabaseProvider.getBankAccountService().getBankAccountsOfUser(user, 100);
        var bankAccountTable = bankAccounts.stream()
                .map(account -> (HtmlTag) HtmlTableRow.row(
                        HtmlDataCell.cell(account.getName()),
                        HtmlDataCell.cell(Localization.getInstance().formatCurrency(account.getBalance()))
                ))
                .toList();

        report.replace("account", HtmlCompound.compound(bankAccountTable));
        report.replace("transactions", transactions(user, bankAccounts));

        try {
            Files.writeString(this.file.toPath(), report.build());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public File getFile() {
        return file;
    }

    private HtmlCompound transactions(User user, List<BankAccount> accounts) {
        List<HtmlTag> tags = new ArrayList<>();


        for (BankAccount account : accounts) {
            tags.add(HtmlTableRow.row(
                    HtmlDataCell.head(account.getName()),
                    HtmlDataCell.head(Localization.getInstance().getMessage("report_date")),
                    HtmlDataCell.head(Localization.getInstance().getMessage("report_description")),
                    HtmlDataCell.head(Localization.getInstance().getMessage("report_money"))
            ));

            var list = DatabaseProvider.getTransactionService().getTransactions(user, account, 100, 0).stream()
                    .map(x -> (HtmlTag) HtmlTableRow.row(
                            HtmlDataCell.cell(""),
                            HtmlDataCell.cell(Localization.getInstance().formatInstant(x.getCreated())),
                            HtmlDataCell.cell(x.getDescription()),
                            HtmlDataCell.cell(Localization.getInstance().formatCurrency(x.getMoney()))
                    ))
                    .toList();
            tags.add(HtmlCompound.compound(list));
        }

        return HtmlCompound.compound(tags);
    }
}
