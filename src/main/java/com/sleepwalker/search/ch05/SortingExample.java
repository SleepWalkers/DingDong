package com.sleepwalker.search.ch05;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

public class SortingExample {

    private Directory directory;

    public SortingExample(Directory directory) {
        this.directory = directory;
    }

    public void displayResults(Query query, Sort sort) throws IOException {
        IndexSearcher searcher = new IndexSearcher(directory);

        searcher.setDefaultFieldSortScoring(true, false);

        TopDocs results = searcher.search(query, null, 20, sort);

        System.out.println("\nResults for: " + query.toString() + " sorted by " + sort);

        System.out.println(StringUtils.rightPad("Title", 30) + StringUtils.rightPad("pubmonth", 10)
                           + StringUtils.center("id", 4) + StringUtils.center("score", 15));

        PrintStream out = new PrintStream(System.out, true, "UTF-8");

        DecimalFormat scoreFormatter = new DecimalFormat("0.######");
        for (ScoreDoc sd : results.scoreDocs) {
            int docId = sd.doc;
            float score = sd.score;
            Document doc = searcher.doc(docId);
            System.out
                .println(StringUtils.rightPad(StringUtils.abbreviate(doc.get("title"), 29), 30)
                         + StringUtils.rightPad(doc.get("pubmonth"), 10)
                         + StringUtils.center("" + docId, 4)
                         + StringUtils.center(scoreFormatter.format(score), 12));

            out.println("    " + doc.get("category"));
        }
        searcher.close();
    }
}
