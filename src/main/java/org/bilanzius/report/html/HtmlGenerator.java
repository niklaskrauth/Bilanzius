package org.bilanzius.report.html;

import org.bilanzius.utils.Localization;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class HtmlGenerator
{

    public static HtmlGenerator create()
    {
        return new HtmlGenerator();
    }

    private String htmlCode;
    private final Map<String, HtmlTag> replacements = new HashMap<>();

    private HtmlGenerator()
    {
    }

    public HtmlGenerator html(String htmlCode)
    {
        this.htmlCode =
                htmlCode;
        return this;
    }

    public HtmlGenerator htmlFromResources(String name)
    {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        var url =
                classloader.getResource(name);

        if (url == null) {
            throw new IllegalStateException("URL is null");
        }

        try {
            var path =
                    Paths.get(url.toURI());
            var content =
                    Files.readString(path, StandardCharsets.UTF_8);

            return html(content);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public HtmlGenerator replace(String key, HtmlTag tag)
    {
        this.replacements.put(key, tag);
        return this;
    }

    public String build()
    {
        if (this.htmlCode == null) {
            throw new RuntimeException("HTML code is null.");
        }

        var result =
                this.htmlCode;
        result =
                insertHtmlTags(result);
        result =
                replaceLanguageKeys(result);

        return result;
    }

    private String insertHtmlTags(String input)
    {
        for (Map.Entry<String, HtmlTag> entry : this.replacements.entrySet()) {
            input = input.replace("{" + entry.getKey() + "}", entry.getValue().build());
        }

        return input;
    }

    private String replaceLanguageKeys(String input)
    {
        for (String s :
                Localization.getInstance().keySet()) {
            input = input.replace("[" + s + "]", Localization.getInstance().getMessage(s));
        }
        return input;
    }
}
