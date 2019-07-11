/**
 * 线程启动日志收集
 * 只针对项目内的继承了Runnable的类启动监控
 * 不包含内部匿名类
 */
package com.yq.starter.cat.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import static com.yq.starter.cat.constant.Types.THREAD;

@Aspect
@Slf4j
public class ThreadAspectLogger extends DefaultAspectLogger {

    @Around("execution(* (com.yq..* && java.lang.Runnable+).start())")
    @Override
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return super.doAround(pjp);
    }

    @Override
    public Transaction getTransaction(ProceedingJoinPoint pjp) {
        String className = pjp.getTarget().getClass().getName();
        return Cat.newTransaction(THREAD.name(), className);
    }
}
