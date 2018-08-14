package com.sleepwalker.search.ch04;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class AnalyzerUtils {

    public static void displayTokens(Analyzer analyzer, String text) throws IOException {
        displayTokens(analyzer.tokenStream("contents,", new StringReader(text)));
    }

    public static void displayTokens(TokenStream stream) throws IOException {
        TermAttribute term = stream.addAttribute(TermAttribute.class);
        while (stream.incrementToken()) {
            System.out.println("[" + term.term() + "]");
        }
    }

    public static void displayTokensWithFullDetails(Analyzer analyzer,
                                                    String text) throws IOException {
        TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));

        TermAttribute term = stream.addAttribute(TermAttribute.class);
        PositionIncrementAttribute positionIncrementAttribute = stream
            .addAttribute(PositionIncrementAttribute.class);
        OffsetAttribute offsetAttribute = stream.addAttribute(OffsetAttribute.class);
        TypeAttribute typeAttribute = stream.addAttribute(TypeAttribute.class);

        int position = 0;
        while (stream.incrementToken()) {
            int increment = positionIncrementAttribute.getPositionIncrement();

            if (increment > 0) {
                position = position + increment;
                System.out.println();
                System.out.print(position + ": ");
            }

            System.out.println("[" + term.term() + ":" + offsetAttribute.startOffset() + "->"
                               + offsetAttribute.endOffset() + ":" + typeAttribute.type() + "]");
        }
    }
}
