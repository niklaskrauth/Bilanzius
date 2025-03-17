package org.bilanzius.report.html;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HtmlTableRowTest {

    @Test
    void testTableRow() {
        var row = HtmlTableRow.row(PlainText.text("1"));

        Assertions.assertEquals("<tr>1</tr>", row.build());
    }
}
