package com.sleepwalker.concurrent;

import java.io.PipedWriter;
import java.util.Random;

public class PipedIO {

}

class Sender implements Runnable {

    private Random      rand = new Random(47);

    private PipedWriter out  = new PipedWriter();

    public PipedWriter getPipedWriter() {
        return out;
    }

    @Override
    public void run() {
        try {

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}