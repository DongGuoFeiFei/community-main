package com.example.communityserver.utils.markdown;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Markdown 处理工具类 (基于 CommonMark 0.21.0)
 * 功能：
 * 1. Markdown 转 HTML
 * 2. 支持表格扩展
 * 3. 可配置的解析和渲染选项
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-26
 **/


public class MarkDownToHtmlUtils {

    private static final List<Extension> DEFAULT_EXTENSIONS =
            Collections.singletonList(TablesExtension.create());

    private static final Parser DEFAULT_PARSER;
    private static final HtmlRenderer DEFAULT_RENDERER;

    static {
        // 初始化默认解析器和渲染器
        DEFAULT_PARSER = Parser.builder()
                .extensions(DEFAULT_EXTENSIONS)
                .build();

        DEFAULT_RENDERER = HtmlRenderer.builder()
                .extensions(DEFAULT_EXTENSIONS)
                .build();
    }

    /**
     * 将Markdown转换为HTML（使用默认配置）
     *
     * @param markdown Markdown文本
     * @return 渲染后的HTML
     */
    public static String markdownToHtml(String markdown) {
        Node document = DEFAULT_PARSER.parse(markdown);
        return DEFAULT_RENDERER.render(document);
    }

    /**
     * 自定义配置的Markdown解析器
     *
     * @param extensions 要使用的扩展列表
     * @return 配置好的Parser实例
     */
    public static Parser createParser(List<Extension> extensions) {
        return Parser.builder()
                .extensions(extensions)
                .build();
    }

    /**
     * 自定义配置的HTML渲染器
     *
     * @param extensions 要使用的扩展列表
     * @return 配置好的HtmlRenderer实例
     */
    public static HtmlRenderer createRenderer(List<Extension> extensions) {
        return HtmlRenderer.builder()
                .extensions(extensions)
                .build();
    }

    /**
     * 使用自定义解析器和渲染器转换Markdown
     *
     * @param markdown Markdown文本
     * @param parser   自定义解析器
     * @param renderer 自定义渲染器
     * @return 渲染后的HTML
     */
    public static String markdownToHtml(String markdown, Parser parser, HtmlRenderer renderer) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    /**
     * 获取默认的Markdown解析器
     */
    public static Parser getDefaultParser() {
        return DEFAULT_PARSER;
    }

    /**
     * 获取默认的HTML渲染器
     */
    public static HtmlRenderer getDefaultRenderer() {
        return DEFAULT_RENDERER;
    }

    /**
     * 获取默认的扩展列表
     */
    public static List<Extension> getDefaultExtensions() {
        return DEFAULT_EXTENSIONS;
    }
}