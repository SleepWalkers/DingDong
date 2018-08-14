package com.sleepwalker.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TrackCounter {

    private Map<Integer, Integer> trackCounts = new HashMap<>();

    @Pointcut("execution(* com.sleepwalker.bean.CompactDisc.play(int))")
    public void trackPlayed(int trackId) {
    }

    @After("trackPlayed(trackId)")
    public void test(int trackId) {
        int currentCount = getPlayCount(trackId);
        trackCounts.put(trackId, currentCount + 1);
    }

    @Before("trackPlayed(trackId)")
    public void countTrack(int trackId) {
        int currentCount = getPlayCount(trackId);
        trackCounts.put(trackId, currentCount + 1);
    }

    public int getPlayCount(int trackId) {
        return trackCounts.containsKey(trackId) ? trackCounts.get(trackId) : 0;
    }
}
