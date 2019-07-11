package com.yq.starter.cat.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.github.kevinsawicki.http.HttpRequest;
import com.yq.starter.cat.constant.Types;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @auth: YQ
 * @date： 6/28/2019
 **/
@Aspect
public class HttpRequestAspectLogger extends DefaultAspectLogger {

    public HttpRequestAspectLogger() {
        reentrant = false;
    }

    @Around("execution(public * com.github.kevinsawicki.http.HttpRequest.body(..))" +
            "|| execution(public * com.github.kevinsawicki.http.HttpRequest.bytes(..))" +
            "|| execution(public * com.github.kevinsawicki.http.HttpRequest.buffer(..))")
    @Override
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return super.doAround(pjp);
    }

    @Override
    Transaction getTransaction(ProceedingJoinPoint pjp) {
        HttpRequest httpRequest = (HttpRequest) pjp.getThis();
        return Cat.newTransaction(Types.HTTP_REQUEST.name(), httpRequest.method() + " - " + httpRequest.url());
    }
}
