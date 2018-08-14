package com.sleepwalker.search.ch01;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {

    public static void main(String[] args) throws IOException, ParseException {
        String indexDir = args[0];
        String q = args[1];
        search(indexDir, q);
    }

    public static void search(String indexDir, String q) throws IOException, ParseException {
        Directory directory = FSDirectory.open(new File(indexDir));
        IndexSearcher indexSearcher = new IndexSearcher(directory);
        QueryParser queryParser = new QueryParser(Version.LUCENE_30, "contents",
            new StandardAnalyzer(Version.LUCENE_30));
        Query query = queryParser.parse(q);
        TopDocs topDocs = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(document.get("fullpath"));
        }
        indexSearcher.close();
    }
}
