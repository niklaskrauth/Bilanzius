package org.bilanzius.report.html;

import java.util.List;

public class HtmlCompound implements HtmlTag
{

    public static HtmlCompound compound(List<HtmlTag> tags)
    {
        return new HtmlCompound(tags);
    }

    private final List<HtmlTag> tags;

    private HtmlCompound(List<HtmlTag> tags)
    {
        this.tags = tags;
    }

    @Override
    public String build()
    {
        StringBuilder builder = new StringBuilder();

        for (HtmlTag tag :
                tags)
        {
            builder.append(tag.build());
        }

        return builder.toString();
    }
}
