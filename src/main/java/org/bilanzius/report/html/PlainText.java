package org.bilanzius.report.html;

public class PlainText implements HtmlTag
{

    public static PlainText text(String text)
    {
        return new PlainText(text);
    }

    private final String text;

    private PlainText(String text)
    {
        this.text = text;
    }

    @Override
    public String build()
    {
        return this.text;
    }
}
