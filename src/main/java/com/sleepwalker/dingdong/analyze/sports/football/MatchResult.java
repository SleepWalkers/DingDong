package com.sleepwalker.dingdong.analyze.sports.football;

import java.util.List;

public class MatchResult {

    private List<String> titles;

    private int          singular;

    private int          dual;

    private int          equal;

    private MatchType    matchType;

    @Override
    public String toString() {
        return matchType + "\n单数: " + singular + "\n" + "双数: " + dual + "\n" + "平局: " + equal + "\n"
               + "胜率: " + (singular * 1.00) / ((dual + singular) * 1.00) + "\n";
    }

    public MatchResult(MatchType matchType) {
        this.matchType = matchType;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public int getSingular() {
        return singular;
    }

    public void setSingular(int singular) {
        this.singular = singular;
    }

    public int getDual() {
        return dual;
    }

    public void setDual(int dual) {
        this.dual = dual;
    }

    public int getEqual() {
        return equal;
    }

    public void setEqual(int equal) {
        this.equal = equal;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

}
