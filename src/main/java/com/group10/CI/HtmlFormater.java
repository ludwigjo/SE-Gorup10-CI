package com.group10.CI;

/**
 * Class for formatting HTML with a HTML boilerplate
 */
public class HtmlFormater {

    protected static String htmlFormat = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>%s</title></head><body>%s</body></html>";

    /**
     * Formats a plain HTML site with the header and content provided
     * 
     * @param header  The sites header
     * @param content The displayed info on the site, can be in HTML or plain text
     * @return A formatted html site
     */
    public static String formatHtml(String header, String content) {
        return String.format(htmlFormat, header, content);
    }

    /**
     * Formats a plain HTML site with the header and a unordered list
     * 
     * @param header The sites header
     * @param list   A list that will be displayed on the site
     * @return A formatted html site
     */
    public static String formatHtmlHrefList(String header, String[] list, String prefix) {
        StringBuilder content = new StringBuilder();
        content.append("<ul>");
        for (String item : list) {
            content.append(String.format("<li><a href=\"%s%s\">%s</a></li>", prefix, item, item));
        }
        content.append("</ul>");

        return formatHtml(header, content.toString());
    }

}
