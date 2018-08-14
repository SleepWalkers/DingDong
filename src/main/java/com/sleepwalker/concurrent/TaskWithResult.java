package com.sleepwalker.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class TaskWithResult implements Callable<String> {

    @Override
    public String call() throws Exception {
        TimeUnit.MILLISECONDS.sleep(10000);
        return "result of TaskWithResult " + id;
    }

    private int id;

    public TaskWithResult(int id) {
        this.id = id;
    }

}
