package com.sleepwalker.core.spring.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 同步令牌验证，防止CSRF攻击 和 重复提交
 * <p>
 * 适用于防止重复提交<br/>
 * 同时需要在新建的页面中添加
 * <input type="hidden" name="token" value="${token}">
 * </p>
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckToken {

    /**
     * 如果重置Token为true，则请求在当前页面无法发送第二次，可避免表单重复
     * @return
     */
    boolean resetToken() default false;
}
