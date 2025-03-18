package org.bilanzius.report.html;

public class HtmlDataCell implements HtmlTag
{

    public static HtmlDataCell cell(String content)
    {
        return new HtmlDataCell("td", content);
    }

    public static HtmlDataCell head(String content)
    {
        return new HtmlDataCell("th", content);
    }

    private final String tag;
    private final String content;

    private HtmlDataCell(String tag, String content)
    {
        this.tag = tag;
        this.content =
                content;
    }

    @Override
    public String build()
    {
        return "<" + this.tag + ">" +
                this.content +
                "</" + this.tag + ">";
    }
}
