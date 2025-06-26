package com.example.communityserver.utils.markdown;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;

import java.util.ArrayList;
import java.util.List;
/**
 * <p>
 * Markdown纯文本提取工具类
 * 功能：从Markdown中提取纯文本，去除所有格式和媒体
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-26
 **/


public class MarkDownToTextUtils {

    private static final Parser PARSER = Parser.builder().build();

    /**
     * 将Markdown转换为纯文本
     * @param markdown Markdown文本
     * @return 纯文本内容
     */
    public static String markdownToText(String markdown) {
        Node document = PARSER.parse(markdown);
        TextContentVisitor visitor = new TextContentVisitor();
        document.accept(visitor);
        return visitor.getText();
    }

    /**
     * 自定义Visitor用于提取文本
     */
    private static class TextContentVisitor extends AbstractVisitor {
        private final StringBuilder textBuilder = new StringBuilder();
        private final List<String> lineBuffer = new ArrayList<>();

        @Override
        public void visit(Text text) {
            lineBuffer.add(text.getLiteral());
        }

        @Override
        public void visit(SoftLineBreak softLineBreak) {
            flushLineBuffer();
            textBuilder.append(' ');
        }

        @Override
        public void visit(HardLineBreak hardLineBreak) {
            flushLineBuffer();
            textBuilder.append('\n');
        }

        @Override
        public void visit(Paragraph paragraph) {
            super.visit(paragraph);
            flushLineBuffer();
            textBuilder.append('\n');
        }

        @Override
        public void visit(Link link) {
            // 跳过链接文本，只处理链接内的文本
            visitChildren(link);
        }

        @Override
        public void visit(Image image) {
            // 完全跳过图片
        }

        @Override
        public void visit(Code code) {
            lineBuffer.add(code.getLiteral());
        }

        @Override
        public void visit(Emphasis emphasis) {
            visitChildren(emphasis);
        }

        @Override
        public void visit(StrongEmphasis strongEmphasis) {
            visitChildren(strongEmphasis);
        }

        @Override
        public void visit(BlockQuote blockQuote) {
            visitChildren(blockQuote);
            flushLineBuffer();
        }

        @Override
        public void visit(BulletList bulletList) {
            visitChildren(bulletList);
            flushLineBuffer();
        }

        @Override
        public void visit(OrderedList orderedList) {
            visitChildren(orderedList);
            flushLineBuffer();
        }

        @Override
        public void visit(ListItem listItem) {
            visitChildren(listItem);
            flushLineBuffer();
        }

        @Override
        public void visit(Heading heading) {
            visitChildren(heading);
            flushLineBuffer();
            textBuilder.append('\n');
        }

        private void flushLineBuffer() {
            if (!lineBuffer.isEmpty()) {
                String line = String.join("", lineBuffer).trim();
                if (!line.isEmpty()) {
                    if (textBuilder.length() > 0) {
                        textBuilder.append(' ');
                    }
                    textBuilder.append(line);
                }
                lineBuffer.clear();
            }
        }

        public String getText() {
            flushLineBuffer();
            return textBuilder.toString().trim();
        }
    }
}