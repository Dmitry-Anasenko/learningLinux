package ru.hiik.learninglinux;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Класс для парсинга Markdown (зависит от commonmark-java)
 * @author dmitry
 */
public class MarkdownParser {
    
    private static final Parser parser = Parser.builder().build();
    
    /**
     * Парсинг Markdown
     * @param md Текст в формате Markdown
     * @return Текст в формате HTML
     */
    public static String parse(String md) {
        Node document = parser.parse(md);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
