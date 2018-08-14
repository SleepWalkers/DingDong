package com.sleepwalker.search.ch03;

import java.io.IOException;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class BasicSeachingTest {

    public void testTerm() throws IOException {
        Directory directory = new RAMDirectory();
        IndexSearcher indexSearcher = new IndexSearcher(directory);

        Term term = new Term("subject", "ant");
        Query query = new TermQuery(term);
        TopDocs topDocs = indexSearcher.search(query, 10);

        indexSearcher.search(new TermQuery(new Term("subject", "junit")), 10);

        indexSearcher.close();
        directory.close();
    }

    public void testQueryParse() throws Exception {

        Directory directory = new RAMDirectory();
        IndexSearcher indexSearcher = new IndexSearcher(directory);

        QueryParser parser = new QueryParser(Version.LUCENE_30, "contents", new SimpleAnalyzer());

        Query query = parser.parse("+JUNIT +ANT -MOCK");
        TopDocs topDocs = indexSearcher.search(query, 10);

        Document document = indexSearcher.doc(topDocs.scoreDocs[0].doc);

        query = parser.parse("mock OR junit");

        topDocs = indexSearcher.search(query, 10);

        document = indexSearcher.doc(topDocs.scoreDocs[0].doc);

        indexSearcher.close();
        directory.close();

    }
}
