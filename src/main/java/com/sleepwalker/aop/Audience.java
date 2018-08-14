package com.sleepwalker.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

public class Audience {

    @Pointcut("execution(** concert.Performance.perform(..))")
    public void performance() {
    }

    @Before("performance()")
    public void takeSeats() {
        System.out.println("take seats");
    }

    @Before("performance()")
    public void turnOffCellPhones() {
        System.out.println("turn off cell phones");
    }

    @AfterReturning("performance()")
    public void applaud() {
        System.out.println("show is over");
    }

    @AfterThrowing("performance()")
    public void demandRefund() {
        System.out.println("what a fucking show");
    }

    @Around("performance()")
    public void watchPerformance(ProceedingJoinPoint jp) {
        try {
            System.out.println("take seats");

            System.out.println("turn off cell phones");

            jp.proceed();

            System.out.println("show is over");

        } catch (Throwable e) {

            System.out.println("what a fucking show");
        }
    }

}
