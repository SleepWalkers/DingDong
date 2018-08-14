package com.sleepwalker.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicityTest implements Runnable {

    private AtomicInteger i = new AtomicInteger(0);

    public int getValue() {
        return i.get();
    }

    private synchronized void evenIncrement() {
    }

    @Override
    public void run() {
        while (true) {
            evenIncrement();
        }
    }

}
