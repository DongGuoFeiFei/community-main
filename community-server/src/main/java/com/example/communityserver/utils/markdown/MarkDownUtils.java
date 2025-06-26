package com.example.communityserver.utils.markdown;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 多功能 Markdown 处理工具类
 * 功能：
 * 1. Markdown 转 HTML（支持表格）
 * 2. 提取纯文本（去除所有格式）
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-26
 **/


public class MarkDownUtils {

    // 默认扩展（表格支持）
    private static final List<Extension> DEFAULT_EXTENSIONS =
            Collections.singletonList(TablesExtension.create());

    // 默认解析器和渲染器（用于HTML转换）
    private static final Parser DEFAULT_HTML_PARSER;
    private static final HtmlRenderer DEFAULT_HTML_RENDERER;

    // 纯文本提取用的解析器（不需要扩展）
    private static final Parser TEXT_PARSER;

    static {
        DEFAULT_HTML_PARSER = Parser.builder()
                .extensions(DEFAULT_EXTENSIONS)
                .build();

        DEFAULT_HTML_RENDERER = HtmlRenderer.builder()
                .extensions(DEFAULT_EXTENSIONS)
                .build();

        TEXT_PARSER = Parser.builder().build();
    }

    /* ========== HTML 转换相关方法 ========== */

    /**
     * 将Markdown转换为HTML（使用默认配置，含表格支持）
     */
    public static String toHtml(String markdown) {
        Node document = DEFAULT_HTML_PARSER.parse(markdown);
        return DEFAULT_HTML_RENDERER.render(document);
    }

    /**
     * 自定义配置的Markdown转HTML
     */
    public static String toHtml(String markdown, Parser parser, HtmlRenderer renderer) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    /* ========== 纯文本提取相关方法 ========== */

    /**
     * 从Markdown中提取纯文本（去除所有格式和媒体）
     */
    public static String toPlainText(String markdown) {
        Node document = TEXT_PARSER.parse(markdown);
        TextExtractor extractor = new TextExtractor();
        document.accept(extractor);
        return extractor.getText();
    }

    public static Parser getDefaultHtmlParser() {
        return DEFAULT_HTML_PARSER;
    }

    /* ========== 获取默认实例的方法 ========== */

    public static HtmlRenderer getDefaultHtmlRenderer() {
        return DEFAULT_HTML_RENDERER;
    }

    public static List<Extension> getDefaultExtensions() {
        return DEFAULT_EXTENSIONS;
    }

    /**
     * 纯文本提取器（Visitor模式实现）
     */
    private static class TextExtractor extends AbstractVisitor {
        private final StringBuilder buffer = new StringBuilder();
        private boolean lastWasWhitespace = false;

        @Override
        public void visit(Text text) {
            String content = text.getLiteral();
            buffer.append(content);
            lastWasWhitespace = false;
        }

        @Override
        public void visit(SoftLineBreak softLineBreak) {
            appendWhitespace(" ");
        }

        @Override
        public void visit(HardLineBreak hardLineBreak) {
            appendWhitespace("\n");
        }

        @Override
        public void visit(Paragraph paragraph) {
            super.visit(paragraph);
            appendWhitespace("\n");
        }

        @Override
        public void visit(Heading heading) {
            super.visit(heading);
            appendWhitespace("\n");
        }

        @Override
        public void visit(BulletList bulletList) {
            super.visit(bulletList);
            appendWhitespace("\n");
        }

        @Override
        public void visit(OrderedList orderedList) {
            super.visit(orderedList);
            appendWhitespace("\n");
        }

        @Override
        public void visit(ListItem listItem) {
            super.visit(listItem);
            appendWhitespace("\n");
        }

        // 跳过不需要的节点类型
        @Override
        public void visit(Link link) {
        }

        @Override
        public void visit(Image image) {
        }

        @Override
        public void visit(Code code) {
        }

        @Override
        public void visit(HtmlInline htmlInline) {
        }

        @Override
        public void visit(HtmlBlock htmlBlock) {
        }

        private void appendWhitespace(String whitespace) {
            if (!lastWasWhitespace) {
                buffer.append(whitespace);
                lastWasWhitespace = true;
            }
        }

        public String getText() {
            return buffer.toString().trim();
        }
    }
}