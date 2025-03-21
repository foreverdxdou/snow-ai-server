package com.dxdou.snowai.config;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * NLP配置类
 *
 * @author foreverdxdou
 */
@Configuration
public class NlpConfig {

    @Bean
    public StanfordCoreNLP stanfordCoreNLP() {
        Properties props = new Properties();
        // 设置中文分词器
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,dcoref");
        props.setProperty("tokenize.language", "zh");
        props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/chinese-distsim/chinese-distsim.tagger");
        props.setProperty("parse.model", "edu/stanford/nlp/models/lexparser/chinesePCFG.ser.gz");
        props.setProperty("ner.model", "edu/stanford/nlp/models/ner/chinese.misc.distsim.crf.ser.gz");
        props.setProperty("ner.useSUTime", "false");
        props.setProperty("ner.applyNumericClassifiers", "false");
        props.setProperty("ner.buildEntityMentions", "false");
        props.setProperty("dcoref.demonymPath", "edu/stanford/nlp/models/dcoref/demonyms.txt");
        props.setProperty("dcoref.states", "edu/stanford/nlp/models/dcoref/state_list.txt");
        props.setProperty("dcoref.counts", "edu/stanford/nlp/models/dcoref/demonym.txt");
        props.setProperty("dcoref.animate", "edu/stanford/nlp/models/dcoref/animate.unigrams.txt");
        props.setProperty("dcoref.inanimate", "edu/stanford/nlp/models/dcoref/inanimate.unigrams.txt");
        props.setProperty("dcoref.neutral", "edu/stanford/nlp/models/dcoref/neutral.unigrams.txt");
        props.setProperty("dcoref.male", "edu/stanford/nlp/models/dcoref/male.txt");
        props.setProperty("dcoref.female", "edu/stanford/nlp/models/dcoref/female.txt");
        props.setProperty("dcoref.neutral", "edu/stanford/nlp/models/dcoref/neutral.txt");
        props.setProperty("dcoref.plural", "edu/stanford/nlp/models/dcoref/plural.txt");
        props.setProperty("dcoref.singular", "edu/stanford/nlp/models/dcoref/singular.txt");

        return new StanfordCoreNLP(props);
    }
}