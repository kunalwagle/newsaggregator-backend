package com.newsaggregator.ml.nlp.apache;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunalwagle on 23/05/2017.
 */
public class ApacheParser {

    private Parser parser;

    public ApacheParser() {
        InputStream modelInParse = null;
        try {
            //load chunking model
            modelInParse = getClass().getResourceAsStream("/en-parser-chunking.bin");
            ParserModel model = new ParserModel(modelInParse);

            //create parse tree
            parser = ParserFactory.create(model);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (modelInParse != null) {
                try {
                    modelInParse.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public List<String> getNounPhrases(List<String> sentences) {

        List<String> result = new ArrayList<>();

        for (String sentence : sentences) {

            Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

            //call subroutine to extract noun phrases
            for (Parse p : topParses)
                result.addAll(getNounPhrases(p));

        }

        return result;
    }

    public List<String> getNounPhrases(List<String> sentences) {

        List<String> result = new ArrayList<>();

        for (String sentence : sentences) {

            Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

            //call subroutine to extract noun phrases
            for (Parse p : topParses)
                result.addAll(getNounPhrases(p));

        }

        return result;
    }

    private static List<String> getNounPhrases(Parse p) {

        List<String> nounPhrases = new ArrayList<>();

        if (p.getType().equals("NP")) {
            nounPhrases.add(p.getCoveredText());
        }
        for (Parse child : p.getChildren())
            nounPhrases.addAll(getNounPhrases(child));

        return nounPhrases;
    }

}
