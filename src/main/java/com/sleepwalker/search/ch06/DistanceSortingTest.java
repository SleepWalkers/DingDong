package com.sleepwalker.search.ch06;

import java.io.IOException;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;

import junit.framework.TestCase;

public class DistanceSortingTest extends TestCase {

    private RAMDirectory  directory;

    private IndexSearcher searcher;

    private Query         query;

    @Override
    protected void setUp() throws Exception {

        directory = new RAMDirectory();

        IndexWriter writer = new IndexWriter(directory, new WhitespaceAnalyzer(),
            IndexWriter.MaxFieldLength.UNLIMITED);

        addPoint(writer, "El Charro", "restaurant", 1, 2);
        addPoint(writer, "Cafe Poca Cosa", "restaurant", 5, 9);
        addPoint(writer, "Los Betos", "restaurant", 9, 6);
        addPoint(writer, "Nico's Taco Shop", "restaurant", 3, 8);
        writer.close();
        searcher = new IndexSearcher(directory);
        query = new TermQuery(new Term("type", "restaurant"));

    }

    public void testNearestRestaurantToHome() throws Exception {
        Sort sort = new Sort(new SortField("location", new DistanceComparatorSource(0, 0)));
        TopDocs hits = searcher.search(query, null, 10, sort);

        assertEquals("closest", "El Charro", searcher.doc(hits.scoreDocs[0].doc).get("name"));
        assertEquals("furthest", "Los Betos", searcher.doc(hits.scoreDocs[3].doc).get("name"));
    }

    private void addPoint(IndexWriter writer, String name, String type, int x,
                          int y) throws IOException {
        Document doc = new Document();
        doc.add(new Field("name", name, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("type", name, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("location", name, Field.Store.YES, Field.Index.NOT_ANALYZED));
        writer.addDocument(doc);
    }
}
