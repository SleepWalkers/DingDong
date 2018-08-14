package com.sleepwalker.bean;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class CDPlayer implements MediaPlayer {

    @Resource
    private CompactDisc blankDisc;

    public static void main(String[] args) {
        String aString = "123124abcd";
        System.out.println(aString.substring(0, aString.length() - 4));
    }

    @Override
    public void play() {
        blankDisc.play();
    }
}
