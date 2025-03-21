package com.dxdou.snowai.service.impl;

import com.dxdou.snowai.domain.model.DocumentProcessResult;
import com.dxdou.snowai.service.KbDocumentProcessService;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 知识库文档处理服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class KbDocumentProcessServiceImpl implements KbDocumentProcessService {

    private final StanfordCoreNLP nlpPipeline;

    @Override
    public String generateSummary(String content, int maxLength) {
        if (!StringUtils.hasText(content)) {
            return "";
        }

        // 1. 对文本进行分词和分句
        Annotation document = new Annotation(content);
        nlpPipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        // 2. 计算每个句子的重要性分数
        Map<CoreMap, Double> sentenceScores = new HashMap<>();
        for (CoreMap sentence : sentences) {
            double score = calculateSentenceScore(sentence);
            sentenceScores.put(sentence, score);
        }

        // 3. 选择最重要的句子
        List<CoreMap> selectedSentences = sentenceScores.entrySet().stream()
                .sorted(Map.Entry.<CoreMap, Double>comparingByValue().reversed())
                .limit(maxLength)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 4. 按原始顺序排序并生成摘要
        selectedSentences.sort(Comparator.comparingInt(sentences::indexOf));
        return selectedSentences.stream()
                .map(sentence -> sentence.get(CoreAnnotations.TextAnnotation.class))
                .collect(Collectors.joining("。"));
    }

    @Override
    public String extractKeywords(String content, int maxKeywords) {
        if (!StringUtils.hasText(content)) {
            return "";
        }

        // 1. 对文本进行分词和词性标注
        Annotation document = new Annotation(content);
        nlpPipeline.annotate(document);
        List<CoreLabel> tokens = document.get(CoreAnnotations.TokensAnnotation.class);

        // 2. 统计词频
        Map<String, Integer> wordFreq = new HashMap<>();
        for (CoreLabel token : tokens) {
            String word = token.get(CoreAnnotations.TextAnnotation.class);
            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

            // 只统计名词、动词、形容词
            if (pos.startsWith("NN") || pos.startsWith("VB") || pos.startsWith("JJ")) {
                wordFreq.merge(word, 1, Integer::sum);
            }
        }

        // 3. 选择频率最高的词作为关键词
        return wordFreq.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(maxKeywords)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(","));
    }

    @Override
    public DocumentProcessResult processDocument(String content, int maxSummaryLength, int maxKeywords) {
        DocumentProcessResult result = new DocumentProcessResult();
        try {
            // 1. 生成摘要
            String summary = generateSummary(content, maxSummaryLength);
            result.setSummary(summary);

            // 2. 提取关键词
            String keywords = extractKeywords(content, maxKeywords);
            result.setKeywords(keywords);

            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 计算句子的重要性分数
     *
     * @param sentence 句子
     * @return 重要性分数
     */
    private double calculateSentenceScore(CoreMap sentence) {
        // 1. 获取句子中的词
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);

        // 2. 计算句子长度分数
        double lengthScore = Math.min(tokens.size() / 10.0, 1.0);

        // 3. 计算词性分数
        double posScore = 0.0;
        for (CoreLabel token : tokens) {
            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            if (pos.startsWith("NN") || pos.startsWith("VB") || pos.startsWith("JJ")) {
                posScore += 1.0;
            }
        }
        posScore = posScore / tokens.size();

        // 4. 综合计算最终分数
        return (lengthScore + posScore) / 2.0;
    }
}