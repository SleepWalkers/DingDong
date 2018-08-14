package com.sleepwalker.aop;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

public class AudienceAdvice implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {

    private Audience audience;

    public AudienceAdvice() {
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {

        audience.takeSeats();
        audience.turnOffCellPhones();
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args,
                               Object target) throws Throwable {
        audience.applaud();
    }

    public void afterThrowing(Throwable throwable) {
        audience.demandRefund();
    }

}
