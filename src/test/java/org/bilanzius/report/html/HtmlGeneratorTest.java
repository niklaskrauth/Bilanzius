package org.bilanzius.report.html;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HtmlGeneratorTest {

    @Test
    void generate() {
        var html = HtmlGenerator.create()
                .htmlFromResources("report.html")
                .build();
        Assertions.assertFalse(html == null || html.isBlank());
    }

    @Test
    void generateFromInvalidResources() {
        Assertions.assertThrows(RuntimeException.class, () -> HtmlGenerator.create().htmlFromResources("invalid.html").build());
    }

}
