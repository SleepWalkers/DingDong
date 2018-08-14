package com.sleepwalker.search.ch01;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {

    private IndexWriter indexWriter;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }

        String indexDir = args[0];
        String dataDir = args[1];

        long start = System.currentTimeMillis();
        Indexer indexer = new Indexer(indexDir);
        int numIndexed;
        try {
            numIndexed = indexer.index(dataDir, new TextFilesFilter());
        } finally {
            indexer.close();
        }
        long end = System.currentTimeMillis();

        System.out
            .println("Indexing " + numIndexed + " files took :" + (end - start) + " milliseconds");
    }

    public Indexer(String indexDir) throws IOException {
        Directory directory = FSDirectory.open(new File(indexDir));
        indexWriter = new IndexWriter(directory, new StandardAnalyzer(Version.LUCENE_30), true,
            IndexWriter.MaxFieldLength.UNLIMITED);
    }

    public void close() throws IOException {
        indexWriter.close();
    }

    public int index(String dataDir, FileFilter fileFilter) throws Exception {
        File[] files = new File(dataDir).listFiles();

        for (File file : files) {
            if (!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead()
                && (fileFilter == null || fileFilter.accept(file))) {
                indexFile(file);
            }
        }
        return indexWriter.numDocs();
    }

    private static class TextFilesFilter implements FileFilter {
        @Override
        public boolean accept(File path) {
            return path.getName().toLowerCase().endsWith(".txt");
        }
    }

    private void indexFile(File f) throws Exception {
        System.out.println("Indexing " + f.getCanonicalPath());
        Document document = getDocument(f);
        indexWriter.addDocument(document);
    }

    protected Document getDocument(File f) throws Exception {
        Document document = new Document();
        document.add(new Field("contents", new FileReader(f)));
        document.add(new Field("filename", f.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(
            new Field("fullpath", f.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        return document;
    }
}
