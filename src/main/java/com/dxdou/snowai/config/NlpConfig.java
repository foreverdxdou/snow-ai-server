package com.dxdou.snowai.config;

import org.springframework.context.annotation.Configuration;

/**
 * NLP配置类
 *
 * @author foreverdxdou
 */
@Configuration
public class NlpConfig {

//    @Bean
//    public StanfordCoreNLP stanfordCoreNLP() {
//        Properties props = new Properties();
//        // 设置中文分词器
//        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,dcoref");
//        props.setProperty("tokenize.language", "zh");
//        props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/chinese-distsim.tagger");
//        props.setProperty("parse.model", "edu/stanford/nlp/models/lexparser/chinesePCFG.ser.gz");
//        props.setProperty("ner.model", "edu/stanford/nlp/models/ner/chinese.misc.distsim.crf.ser.gz");
//        props.setProperty("ner.useSUTime", "false");
//        props.setProperty("ner.applyNumericClassifiers", "false");
//        props.setProperty("ner.buildEntityMentions", "false");
//        props.setProperty("dcoref.demonymPath", "edu/stanford/nlp/models/dcoref/demonyms.txt");
//        props.setProperty("dcoref.animate", "edu/stanford/nlp/models/dcoref/animate.unigrams.txt");
//        props.setProperty("dcoref.inanimate", "edu/stanford/nlp/models/dcoref/inanimate.unigrams.txt");
//        props.setProperty("dcoref.neutral", "edu/stanford/nlp/models/dcoref/neutral.unigrams.txt");
//        props.setProperty("segment.model", "edu/stanford/nlp/models/segmenter/chinese/ctb.gz");
//
//        props.setProperty("regexner.model", "edu/stanford/nlp/models/kbp/chinese/gazetteers/cn_regexner_mapping.tab");
//        return new StanfordCoreNLP(props);
//    }

//    @Bean
//    public StanfordCoreNLP stanfordCoreNLP() {
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
//        props.setProperty("coref.algorithm", "neural");
//        return new StanfordCoreNLP(props);
//    }
}