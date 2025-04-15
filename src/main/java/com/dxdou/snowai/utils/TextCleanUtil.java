package com.dxdou.snowai.utils;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import jakarta.xml.bind.DatatypeConverter;
import org.jsoup.Jsoup;
import org.springframework.data.redis.core.RedisTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * 数据清洗工具类
 *
 * @author foreverdxdou
 */
public class TextCleanUtil {

    /**
     * 正则表达式去除非打印字符
     *
     * @param text
     * @return
     */
    public static String removeSpecialChars(String text) {
        return text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
    }

    /**
     * 使用Jsoup去除HTML标签
     *
     * @param htmlContent
     * @return
     */
    public static String cleanHtml(String htmlContent) {
        return Jsoup.parse(htmlContent).text();
    }

    /**
     * 脱敏手机号（中国）
     *
     * @param text
     * @return
     */
    public static String desensitizePhone(String text) {
        return text.replaceAll("(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\\d{8}",
                "***-****-****");
    }

    /**
     * 脱敏身份证号
     *
     * @param text
     * @return
     */
    public static String desensitizeId(String text) {
        return text.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1****$2");
    }

    /**
     * 使用Stanford NLP识别并脱敏人名
     *
     * @param text
     * @return
     */
    public static String desensitizeNER(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        CoreDocument doc = new CoreDocument(text);
        pipeline.annotate(doc);

        StringBuilder sb = new StringBuilder();
        for (CoreEntityMention em : doc.entityMentions()) {
            if (em.entityType().equals("PERSON")) {
                sb.append("[PERSON]");
            } else {
                sb.append(em.text());
            }
        }
        return sb.toString();
    }

    /**
     * 使用Java DateTimeFormatter标准化日期
     * @param dateStr
     * @return
     */
    public static String standardizeDate(String dateStr) {
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                return formatter.format(LocalDate.parse(dateStr, formatter));
            } catch (Exception e) {}
        }
        return "Unknown"; // 无法解析的日期标记
    }


    /**
     * 使用MD5进行内容哈希
     *
     * @param content
     * @return
     */
    public static String generateHash(String content) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(content.getBytes());
            return DatatypeConverter.printHexBinary(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 存储哈希值到Redis
     *
     * @param contentHash
     * @param redisTemplate
     * @return
     */
    public static boolean isDuplicate(String contentHash, RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForSet().isMember("content_hashes", contentHash);
    }

    /**
     * 使用向量计算相似度
     * @param newText
     * @param existingText
     * @return
     */
    public static boolean isSemanticDuplicate(String newText, String existingText) {
//        float[] vec1 = embeddingService.getEmbedding(newText);
//        float[] vec2 = embeddingService.getEmbedding(existingText);
//        double similarity = cosineSimilarity(vec1, vec2);
//        return similarity > 0.95; // 阈值可调
        return false;
    }

    /**
     * 数据质量检查规则示例
     * @param cleanedText
     * @return
     */
    public static boolean validateData(String cleanedText) {
        return cleanedText != null
                && !cleanedText.isEmpty()
                && cleanedText.length() > 50 // 最小长度校验
                && cleanedText.matches(".*[a-zA-Z].*"); // 包含字母校验
    }
}
