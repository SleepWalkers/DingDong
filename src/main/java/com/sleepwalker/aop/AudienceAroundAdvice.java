package com.sleepwalker.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class AudienceAroundAdvice implements MethodInterceptor {

    private Audience audience;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            audience.takeSeats();
            audience.turnOffCellPhones();

            Object returnValue = invocation.proceed();

            audience.applaud();

            return returnValue;
        } catch (Exception exception) {
            audience.demandRefund();
            throw exception;
        }
    }

}
