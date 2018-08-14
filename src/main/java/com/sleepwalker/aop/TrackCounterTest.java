package com.sleepwalker.aop;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sleepwalker.bean.CompactDisc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TrackCounterConfig.class)
public class TrackCounterTest {

    @Resource
    private CompactDisc  blankDisc;

    @Resource
    private TrackCounter trackCounter;

    @Test
    public void test() {
        for (int i = 0; i < 5; i++) {
            blankDisc.play(i);
        }
    }
}
