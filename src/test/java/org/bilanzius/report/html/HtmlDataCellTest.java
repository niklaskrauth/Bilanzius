package org.bilanzius.report.html;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HtmlDataCellTest {

    @Test
    void testHead() {
        var component = HtmlDataCell.head("head");

        Assertions.assertEquals("<th>head</th>", component.build());
    }

    @Test
    void testData() {
        var component = HtmlDataCell.cell("data");

        Assertions.assertEquals("<td>data</td>", component.build());
    }
}
