package com.sleepwalker.search.ch06;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparatorSource;
import org.apache.lucene.search.SortField;

public class DistanceComparatorSource extends FieldComparatorSource {

    private int x;

    private int y;

    public DistanceComparatorSource(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public FieldComparator newComparator(String fieldName, int numHits, int sortPos,
                                         boolean reversed) throws IOException {
        return new DistanceScoreDocLookupComparator(fieldName, numHits);
    }

    private class DistanceScoreDocLookupComparator extends FieldComparator {
        private int[]   xDoc, yDoc;

        private float[] values;

        private float   bottom;

        String          fieldName;

        public DistanceScoreDocLookupComparator(String fieldName, int numHits) throws IOException {
            values = new float[numHits];
            this.fieldName = fieldName;
        }

        @Override
        public void setNextReader(IndexReader reader, int docBase) throws IOException {
            xDoc = FieldCache.DEFAULT.getInts(reader, "x");
            yDoc = FieldCache.DEFAULT.getInts(reader, "y");
        }

        private float getDistance(int doc) {
            int deltax = xDoc[doc] - x;
            int deltay = yDoc[doc] - y;
            return (float) Math.sqrt(deltax * deltax + deltay * deltay);
        }

        @Override
        public int compare(int slot1, int slot2) {

            if (values[slot1] < values[slot2])
                return -1;
            if (values[slot1] > values[slot2])
                return 1;
            return 0;
        }

        @Override
        public int compareBottom(int doc) throws IOException {
            float docDistance = getDistance(doc);
            if (bottom < docDistance)
                return -1;
            if (bottom > docDistance)
                return 1;
            return 0;
        }

        @Override
        public void copy(int slot, int doc) throws IOException {
            values[slot] = getDistance(doc);
        }

        @Override
        public void setBottom(int slot) {
            bottom = values[slot];
        }

        @Override
        public Comparable<?> value(int slot) {
            return new Float(values[slot]);
        }

        public int sortType() {
            return SortField.CUSTOM;
        }
    }

    @Override
    public String toString() {
        return "Distance from (" + x + "," + y + ")";
    }
}
