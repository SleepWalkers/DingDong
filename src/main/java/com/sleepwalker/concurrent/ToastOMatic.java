package com.sleepwalker.concurrent;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ToastOMatic {
    public static void main(String[] args) throws InterruptedException {
        ToastQueue dryQueue = new ToastQueue();
        ToastQueue butteredQueue = new ToastQueue();
        ToastQueue finishedQueue = new ToastQueue();

        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Toaster(dryQueue));
        exec.execute(new Butterer(dryQueue, butteredQueue));
        exec.execute(new Jammer(finishedQueue, butteredQueue));
        exec.execute(new Eater(finishedQueue));

        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}

class Toast {
    public enum Status {
                        DRY, BUTTERED, JAMMED
    }

    private Status    status = Status.DRY;

    private final int id;

    public Toast(int idn) {
        this.id = idn;
    }

    public void butter() {
        status = Status.BUTTERED;
    }

    public void jam() {
        status = Status.JAMMED;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Toast " + id + " : " + status;
    }
}

class ToastQueue extends LinkedBlockingQueue<Toast> {

    /** @author sleepwalker 2017年4月6日 下午6:57:36 */
    private static final long serialVersionUID = -2584619604048641988L;
}

class Toaster implements Runnable {

    private ToastQueue toastQueue;

    private int        count = 0;

    private Random     rand  = new Random(47);

    public Toaster(ToastQueue toastQueue) {
        this.toastQueue = toastQueue;
    }

    @Override
    public void run() {

        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(500));
                Toast t = new Toast(count++);
                System.out.println(t);
                toastQueue.put(t);
            }

        } catch (InterruptedException e) {
            System.out.println("Toaster interrupted");
        }
        System.out.println("Toaster off");
    }
}

class Butterer implements Runnable {

    private ToastQueue dryQueue;

    private ToastQueue butteredQueue;

    public Butterer(ToastQueue dryQueue, ToastQueue butteredQueue) {
        this.dryQueue = dryQueue;
        this.butteredQueue = butteredQueue;
    }

    @Override
    public void run() {

        try {
            while (!Thread.interrupted()) {
                Toast t = dryQueue.take();
                t.butter();
                System.out.println(t);
                butteredQueue.put(t);
            }

        } catch (InterruptedException e) {
            System.out.println("Butterer interrupted");
        }
        System.out.println("Butterer off");
    }
}

class Jammer implements Runnable {

    private ToastQueue finishQueue;

    private ToastQueue butteredQueue;

    public Jammer(ToastQueue finishQueue, ToastQueue butteredQueue) {
        this.finishQueue = finishQueue;
        this.butteredQueue = butteredQueue;
    }

    @Override
    public void run() {

        try {
            while (!Thread.interrupted()) {
                Toast t = butteredQueue.take();
                t.jam();
                System.out.println(t);
                finishQueue.put(t);
            }

        } catch (InterruptedException e) {
            System.out.println("Jammer interrupted");
        }
        System.out.println("Jammer off");
    }
}

class Eater implements Runnable {

    private ToastQueue finishQueue;

    private int        counter = 0;

    public Eater(ToastQueue finishQueue) {
        this.finishQueue = finishQueue;
    }

    @Override
    public void run() {

        try {
            while (!Thread.interrupted()) {
                Toast t = finishQueue.take();
                if (t.getId() != counter++ || t.getStatus() != Toast.Status.JAMMED) {
                    System.out.println("Error: " + t);
                } else {
                    System.out.println("Chomp! " + t);
                }
            }

        } catch (InterruptedException e) {
            System.out.println("Eater interrupted");
        }
        System.out.println("Eater off");
    }

}