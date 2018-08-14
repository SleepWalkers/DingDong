package com.sleepwalker.concurrent;

public class EvenGenerator extends IntGenerator {

    private int currentEven = 0;

    @Override
    public int next() {

        ++currentEven;
        ++currentEven;
        return currentEven;
    }

    public static void main(String[] args) {
        EvenChecker.test(new EvenGenerator());
    }

}
