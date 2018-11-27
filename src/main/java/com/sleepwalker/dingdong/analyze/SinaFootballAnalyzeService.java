package com.sleepwalker.dingdong.analyze;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sleepwalker.dingdong.analyze.sports.football.MatchResult;
import com.sleepwalker.dingdong.analyze.sports.football.MatchType;
import com.sleepwalker.utils.HttpClientUtil;

public class SinaFootballAnalyzeService {

    public static void main(String[] args) {
        String url = "http://platform.sina.com.cn/sports_all/client_api";
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("_sport_t_", "livecast"));
        nameValuePairs.add(new BasicNameValuePair("type", "10"));
        nameValuePairs.add(new BasicNameValuePair("app_key", "3571367214"));

        for (int season = 2018; season <= 2018; season++) {
            List<MatchResult> seasonResult = new ArrayList<>();
            List<MatchResult> groupResult = new ArrayList<>();
            List<MatchResult> shootOutResult = new ArrayList<>();
            for (int round = 1; round <= 4; round++) {
                nameValuePairs.add(new BasicNameValuePair("rnd", round + ""));
                nameValuePairs.add(new BasicNameValuePair("season", season + ""));
                MatchType matchType = null;
                if (round <= 6) {
                    nameValuePairs.add(new BasicNameValuePair("_sport_a_", "groupMatchesByType"));
                    nameValuePairs.add(new BasicNameValuePair("use_type", "group"));
                    matchType = MatchType.GROUP_MATCH;
                } else {
                    nameValuePairs.add(new BasicNameValuePair("_sport_a_", "matchesByType"));
                    matchType = MatchType.SHOOT_OUT_MATCH;
                }

                MatchResult matchResult = analyse(url, matchType, nameValuePairs);

                if (round <= 6) {
                    groupResult.add(matchResult);
                } else {
                    shootOutResult.add(matchResult);
                }
                System.out.println(season + "赛季" + matchType + "第" + round + "轮: " + matchResult);
                System.out
                    .println("--------------------------------------------------------------");
            }
            System.out.println(season + "赛季小组赛: ");
            printTotalResult(groupResult);
            System.out.println(season + "赛季淘汰赛: ");
            printTotalResult(shootOutResult);
            seasonResult.addAll(groupResult);
            seasonResult.addAll(shootOutResult);
            System.out.println(season + "赛季: ");
            printTotalResult(seasonResult);
        }
    }

    private static void printTotalResult(List<MatchResult> matchResults) {
        int singular = 0;
        int dual = 0;
        int equal = 0;
        for (MatchResult matchResult : matchResults) {
            singular += matchResult.getSingular();
            dual += matchResult.getDual();
            equal += matchResult.getEqual();
        }
        System.out.println("单数: " + singular);
        System.out.println("双数: " + dual);
        System.out.println("平局: " + equal);
        System.out.println("胜率: " + (singular * 1.00) / ((dual + singular) * 1.00));
    }

    private static MatchResult analyseGroup(String resultJsonStr, MatchType matchType) {
        JSONObject resultJson = JSON.parseObject(resultJsonStr).getJSONObject("result")
            .getJSONObject("data");

        int singular = 0;
        int dual = 0;
        int equal = 0;
        List<String> titles = new ArrayList<>();

        for (int i = 65; i <= 72; i++) {
            String group = String.valueOf((char) i);
            JSONArray jsonArray = resultJson.getJSONArray(group);
            JSONObject[] jsonObjects = jsonArray.toArray(new JSONObject[] {});

            for (JSONObject jsonObject : jsonObjects) {
                int score1 = jsonObject.getIntValue("Score1");
                int score2 = jsonObject.getIntValue("Score2");
                if (score1 == score2) {
                    equal++;
                } else {
                    if ((score1 + score2) % 2 == 0) {
                        dual++;
                    } else {
                        singular++;
                    }
                }
                titles.add(jsonObject.getString("Title"));
            }
        }

        MatchResult matchResult = new MatchResult(matchType);
        matchResult.setDual(dual);
        matchResult.setEqual(equal);
        matchResult.setSingular(singular);
        matchResult.setTitles(titles);
        return matchResult;
    }

    public static MatchResult analyse(String url, MatchType matchType,
                                      List<NameValuePair> nameValuePairs) {

        String resultJsonStr = HttpClientUtil.get(url, nameValuePairs);

        switch (matchType) {
            case GROUP_MATCH: {
                return analyseGroup(resultJsonStr, matchType);
            }
            case SHOOT_OUT_MATCH: {
                return analyseShootOut(resultJsonStr, matchType);
            }

            default:
                break;
        }
        return null;
    }

    private static MatchResult analyseShootOut(String resultJsonStr, MatchType matchType) {

        JSONArray jsonArray = JSON.parseObject(resultJsonStr).getJSONObject("result")
            .getJSONArray("data");

        int singular = 0;
        int dual = 0;
        int equal = 0;
        List<String> titles = new ArrayList<>();

        JSONObject[] jsonObjects = jsonArray.toArray(new JSONObject[] {});

        for (JSONObject jsonObject : jsonObjects) {
            int score1 = jsonObject.getIntValue("Score1");
            int score2 = jsonObject.getIntValue("Score2");
            if (score1 == score2) {
                equal++;
            } else {
                if ((score1 + score2) % 2 == 0) {
                    dual++;
                } else {
                    singular++;
                }
            }
            titles.add(jsonObject.getString("Title"));
        }

        MatchResult matchResult = new MatchResult(matchType);
        matchResult.setDual(dual);
        matchResult.setEqual(equal);
        matchResult.setSingular(singular);
        matchResult.setTitles(titles);
        return matchResult;
    }

}
