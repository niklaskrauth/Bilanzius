package org.bilanzius.report.html;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HtmlDataCellTest
{

    @Test
    void testTableElements()
    {
        var headComponent = HtmlDataCell.head("head");
        var dataComponent = HtmlDataCell.cell("data");

        Assertions.assertEquals("<th>head</th>", headComponent.build());
        Assertions.assertEquals("<td>data</td>", dataComponent.build());
    }

    @Test
    void testData()
    {
        var component = HtmlDataCell.cell("data");

        Assertions.assertEquals("<td>data</td>", component.build());
    }
}
