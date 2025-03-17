package org.bilanzius.report.html;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class HtmlCompoundTest {

    @Test
    void test() {
        var compound = HtmlCompound.compound(Arrays.asList(PlainText.text("1"), PlainText.text("2")));
        Assertions.assertEquals("12", compound.build());
    }
}
