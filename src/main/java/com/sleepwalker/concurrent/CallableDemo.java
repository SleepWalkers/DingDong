package com.sleepwalker.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableDemo extends Thread{
//
//    
//    public static void main(String[] args) {
//
//        ExecutorService exec = Executors.newCachedThreadPool();
//
//        List<Future<String>> results = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            results.add(exec.submit(new TaskWithResult(i)));
//        }
//        for (Future<String> fs : results) {
//            try {
//                System.out.println(fs.get());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } finally {
//                exec.shutdown();
//            }
//        }
//    }

        public static void main(String[] args) {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + " " + i);
                if (i == 30) {
                    Runnable myRunnable = new MyRunnable();
                    Thread thread = new MyThread(myRunnable);
                    thread.start();
                }
            }
        }
    }

    class MyRunnable implements Runnable {
        private int i = 0;

        @Override
        public void run() {
            System.out.println("in MyRunnable run");
            for (i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + " " + i);
            }
        }
    }

    class MyThread extends Thread {

        private int i = 0;
        
        public MyThread(Runnable runnable){
            super(runnable);
        }

        @Override
        public void run() {
            System.out.println("in MyThread run");
            for (i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + " " + i);
            }
        }
    }

