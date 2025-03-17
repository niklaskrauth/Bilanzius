package org.bilanzius.report.html;

import java.util.Arrays;
import java.util.List;

public class HtmlTableRow implements HtmlTag {

    public static HtmlTableRow row(HtmlTag... children) {
        return row(Arrays.asList(children));
    }

    public static HtmlTableRow row(List<HtmlTag> children) {
        return new HtmlTableRow(children);
    }

    private final List<HtmlTag> children;

    private HtmlTableRow(List<HtmlTag> children) {
        this.children = children;
    }

    @Override
    public String build() {
        StringBuilder stringBuilder = new StringBuilder("<tr>");

        for (HtmlTag child : this.children) {
            stringBuilder.append(child.build());
        }

        stringBuilder.append("</tr>");
        return stringBuilder.toString();
    }
}
