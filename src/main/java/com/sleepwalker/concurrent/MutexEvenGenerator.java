package com.sleepwalker.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MutexEvenGenerator extends IntGenerator {

    private int  evenCount = 0;

    private Lock lock      = new ReentrantLock();

    @Override
    public int next() {

        lock.lock();
        try {
            ++evenCount;
            ++evenCount;
            return evenCount;
        } finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        EvenChecker.test(new MutexEvenGenerator());
    }

}
