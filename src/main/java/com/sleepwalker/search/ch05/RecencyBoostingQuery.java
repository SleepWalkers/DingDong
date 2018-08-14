package com.sleepwalker.search.ch05;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.function.CustomScoreProvider;
import org.apache.lucene.search.function.CustomScoreQuery;

public class RecencyBoostingQuery extends CustomScoreQuery {

    double     multiplier;

    int        today;

    int        maxDaysAgo;

    String     dayField;

    static int MSEC_PER_DAY = 1000 * 3600 * 24;

    public RecencyBoostingQuery(Query q, double multiplier, int maxDaysAgo, String dayField) {
        super(q);
        today = (int) (System.currentTimeMillis() / MSEC_PER_DAY);
        this.multiplier = multiplier;
        this.maxDaysAgo = maxDaysAgo;
        this.dayField = dayField;
    }

    private class RecencyBooster extends CustomScoreProvider {

        final int[] publishDay;

        public RecencyBooster(IndexReader reader) throws IOException {
            super(reader);
            publishDay = FieldCache.DEFAULT.getInts(reader, dayField);
        }

        @Override
        public float customScore(int doc, float subQueryScore, float valSrcScore) {
            int daysAgo = today - publishDay[doc];
            if (daysAgo < maxDaysAgo) {
                float boost = (float) (multiplier * (maxDaysAgo - daysAgo) / maxDaysAgo);
                return (float) (subQueryScore * (1.0 + boost));
            } else {
                return subQueryScore;
            }
        }
    }

    @Override
    public CustomScoreProvider getCustomScoreProvider(IndexReader reader) throws IOException {
        return new RecencyBooster(reader);
    }

}
