package com.yq.starter.cat.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.yq.starter.cat.constant.Types;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @auth: YQ
 * @date： 6/28/2019
 **/
@Aspect
public class HttpTemplateAspectLogger extends DefaultAspectLogger {

    @Around("execution(public * org.springframework.web.client.RestTemplate.execute(..))")
    @Override
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return super.doAround(pjp);
    }

    @Override
    Transaction getTransaction(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        return Cat.newTransaction(Types.REST_TEMPLATE.name(), args[1].toString() + " - " + args[0].toString());
    }
}
