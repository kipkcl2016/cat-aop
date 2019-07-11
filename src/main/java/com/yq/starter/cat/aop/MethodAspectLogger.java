package com.yq.starter.cat.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.yq.starter.cat.constant.Types.METHOD;

/**
 * @author yq
 * @date 2019-02-19
 */
@Component
@ConditionalOnProperty(name = "fw.cat.enable", havingValue = "true")
@Aspect
public class MethodAspectLogger extends DefaultAspectLogger {

    public MethodAspectLogger() {
        super(true);
    }

    @Around("@annotation(com.yq.starter.cat.annotation.CatTransaction)")
    @Override
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return super.doAround(pjp);
    }

    /**
     * 使用CAT 打点
     */
    @Override
    public Transaction getTransaction(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        return Cat.newTransaction(METHOD.name(), method.getDeclaringClass().getName() + "." + method.getName());
    }
}


