package ru.hiik.learninglinux;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Класс для парсинга Markdown (зависит от commonmark-java и jsoup)
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
        // Парсинг кода Markdown в код HTML
        Node document = parser.parse(md);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        Document html = Jsoup.parse(renderer.render(document));
        // Парсинг тэгов img для вставки корректного пути к изображению
        Elements tagsImg = html.getElementsByTag("img");
        tagsImg.forEach((tagImg) -> {
            String nameImg = tagImg.attr("src");
            tagImg.attr("src", FileManager.getImages().toURI() + 
                    FileManager.getSeparator() + nameImg);
        });
        return String.valueOf(html);
    }
}
