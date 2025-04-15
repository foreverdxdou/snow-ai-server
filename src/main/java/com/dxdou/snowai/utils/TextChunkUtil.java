package com.dxdou.snowai.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 文本分块工具类
 *
 * @author foreverdxdou
 */
public class TextChunkUtil {
    // 分块配置参数
    private static final int DEFAULT_MAX_CHUNK_SIZE = 1000;    // 默认最大块大小（字符数）
    private static final int DEFAULT_OVERLAP_SIZE = 200;       // 默认重叠大小
    private static final double MIN_SENTENCE_LENGTH = 10.0;   // 最小句子长度阈值

    // 正则表达式模式（预编译提升性能）
    private static final Pattern HEADING_PATTERN = Pattern.compile(
            "^(#{1,6}\\s+.*|第[一二三四五六七八九十]+章\\s+.*|\\d+(\\.\\d+)*\\s+.*|==+\\s+.*)",
            Pattern.MULTILINE
    );

    private static final Pattern SENTENCE_BOUNDARY = Pattern.compile(
            // 中文句子边界：。！？ 英文句子边界：!.?
            // 包含后续可能跟随的引号或括号
            "([。！？?!.．])([’”\"』]?)(\\s*)(?=[^\\p{P}\\p{S}]|$)"
    );

    private static final Pattern PARAGRAPH_SPLITTER = Pattern.compile("\\n{2,}");
    private static final Pattern LINE_BREAK = Pattern.compile("(\\r\\n|\\n)");

    public static List<DocumentChunk> chunkContent(String text) {
        // TODO: 2025/4/15 文本分块逻辑
        return chunkContent(text, DEFAULT_MAX_CHUNK_SIZE, DEFAULT_OVERLAP_SIZE);
    }

    public static List<DocumentChunk> chunkContent(String text, int maxChunkSize, int overlapSize) {
        List<DocumentChunk> result = new ArrayList<>();
        int globalIndex = 0;  // 全局索引计数器

        // 1. 预处理阶段
        String normalizedText = preprocessText(text);

        // 2. 结构分块（按标题分割）
        List<String> sections = splitByHeadings(normalizedText);

        // 3. 段落分块处理
        for (String section : sections) {
            List<String> paragraphs = splitParagraphs(section);

            for (String paragraph : paragraphs) {
                // 4. 句子级别分割
                List<String> sentences = splitSentences(paragraph);

                // 5. 动态分块策略
                List<String> chunks = mergeWithSemanticAwareness(
                        sentences,
                        maxChunkSize,
                        overlapSize
                );

                // 转换分块对象并维护索引
                for (String chunk : chunks) {
                    result.add(new DocumentChunk(globalIndex++, chunk));
                }
            }
        }

        // 6. 后处理保证所有内容被包含
        if (result.isEmpty()) {
            List<String> fixedChunks = splitFixedWithOverlap(
                    normalizedText,
                    maxChunkSize,
                    overlapSize
            );
            for (String chunk : fixedChunks) {
                result.add(new DocumentChunk(globalIndex++, chunk));
            }
        }

        return result;
    }



    public static class DocumentChunk {
        public final int index;
        public final String content;

        public DocumentChunk(int index, String content) {
            this.index = index;
            this.content = content;
        }

        public int getIndex() {
            return index;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "DocumentChunk{index=" + index + ", content='" + content + "'}";
        }
    }

    private static String preprocessText(String text) {
        // 统一换行符为\n，合并连续空白
        return LINE_BREAK.matcher(text)
                .replaceAll("\n")
                .replaceAll("[\\t\\f\\r]+", " ")
                .replaceAll("\\s{2,}", " ")
                .trim();
    }

    private static List<String> splitByHeadings(String text) {
        List<String> sections = new ArrayList<>();
        Matcher matcher = HEADING_PATTERN.matcher(text);
        int lastEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                sections.add(text.substring(lastEnd, matcher.start()));
            }
            lastEnd = matcher.start();
        }

        if (lastEnd < text.length()) {
            sections.add(text.substring(lastEnd));
        }

        return sections.stream()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private static List<String> splitParagraphs(String text) {
        return Arrays.stream(PARAGRAPH_SPLITTER.split(text))
                .map(String::trim)
                .filter(p -> !p.isEmpty())
                .collect(Collectors.toList());
    }

    private static List<String> splitSentences(String paragraph) {
        List<String> sentences = new ArrayList<>();
        Matcher matcher = SENTENCE_BOUNDARY.matcher(paragraph);
        int lastEnd = 0;

        while (matcher.find()) {
            int end = matcher.end();
            // 排除英文缩写（简单处理）
            if (isValidSentenceBoundary(paragraph, end)) {
                sentences.add(paragraph.substring(lastEnd, end).trim());
                lastEnd = end;
            }
        }

        if (lastEnd < paragraph.length()) {
            sentences.add(paragraph.substring(lastEnd).trim());
        }

        return sentences.stream()
                .filter(s -> s.length() > MIN_SENTENCE_LENGTH)
                .collect(Collectors.toList());
    }

    private static boolean isValidSentenceBoundary(String text, int position) {
        // 简单排除英文缩写（可根据需要扩展）
        if (position > 2 &&
                Character.isLetter(text.charAt(position - 2))) {
            char prevChar = text.charAt(position - 2);
            return !(prevChar >= 'A' && prevChar <= 'Z');
        }
        return true;
    }

    private static List<String> mergeWithSemanticAwareness(List<String> sentences,
                                                    int maxSize,
                                                    int overlap) {
        List<String> chunks = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        Deque<String> overlapBuffer = new ArrayDeque<>();

        for (String sentence : sentences) {
            if (buffer.length() + sentence.length() > maxSize) {
                // 添加当前块
                chunks.add(buffer.toString());

                // 处理重叠
                buffer.setLength(0);
                int overlapCount = 0;
                while (!overlapBuffer.isEmpty() && overlapCount < overlap) {
                    String prev = overlapBuffer.pollLast();
                    buffer.insert(0, prev);
                    overlapCount += prev.length();
                }
            }

            buffer.append(sentence).append(" ");
            overlapBuffer.offer(sentence);
            if (overlapBuffer.size() > maxSize / MIN_SENTENCE_LENGTH) {
                overlapBuffer.pollFirst();
            }
        }

        if (buffer.length() > 0) {
            chunks.add(buffer.toString().trim());
        }

        // 处理剩余内容
        if (chunks.isEmpty()) {
            return splitFixedWithOverlap(
                    String.join(" ", sentences),
                    maxSize,
                    overlap
            );
        }

        return chunks;
    }

    private static List<String> splitFixedWithOverlap(String text,
                                               int chunkSize,
                                               int overlap) {
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());
            chunks.add(text.substring(start, end));

            start = end - overlap;
            if (start < 0) start = 0;
        }
        return chunks;
    }
}
