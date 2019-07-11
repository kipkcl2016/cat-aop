package com.yq.starter.cat.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.yq.starter.cat.constant.Types;
import okhttp3.Call;
import okhttp3.Request;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @auth: YQ
 * @date： 6/28/2019
 **/
@Aspect
public class OkHttpAspectLogger extends DefaultAspectLogger {

    @Around("execution(public okhttp3.Response okhttp3.Call+.execute())")
    @Override
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return super.doAround(pjp);
    }

    @Override
    Transaction getTransaction(ProceedingJoinPoint pjp) {
        Request request = ((Call) pjp.getThis()).request();
        return Cat.newTransaction(Types.OK_HTTP.name(), request.method() + " - " + request.url().scheme() + "://" + request.url().host());
    }
}
