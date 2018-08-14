package com.sleepwalker.concurrent;

import java.io.IOException;
import java.util.Random;

public class CachedThreadPool {

    //    public static void main(String[] args) {
    //        ExecutorService exec = Executors.newCachedThreadPool();
    //        for (int i = 0; i < 5; i++) {
    //            exec.execute(new LiftOff());
    //        }
    //        exec.shutdown();
    //
    //    }
    public void main(String[] args) throws IOException, InterruptedException {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextInt(3));
        }
    }
}
