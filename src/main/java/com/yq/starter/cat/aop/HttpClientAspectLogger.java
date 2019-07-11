package com.yq.starter.cat.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.net.URI;

import static com.yq.starter.cat.constant.Types.HTTP_CLIENT;
import static java.util.Objects.nonNull;

/**
 * HttpClient调用日志收集
 */
@Aspect
@Slf4j
public class HttpClientAspectLogger extends DefaultAspectLogger {

    public HttpClientAspectLogger() {
        reentrant = false;
    }

    @Around("execution(public * org.apache.http.client.HttpClient+.execute(..))")
    @Override
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return super.doAround(pjp);
    }

    @Override
    public Transaction getTransaction(ProceedingJoinPoint pjp) throws Exception {
        HttpHost httpHost = null;
        HttpUriRequest uriRequest = null;
        HttpRequest request = null;
        for (Object param : pjp.getArgs()) {
            if (param instanceof HttpHost) {
                httpHost = (HttpHost) param;
            }
            if (param instanceof HttpUriRequest) {
                uriRequest = (HttpUriRequest) param;
            }
            if (param instanceof HttpRequest) {
                request = (HttpRequest) param;
            }
        }
        if (nonNull(uriRequest)) {
            return logTransaction(uriRequest.getURI(), uriRequest.getMethod());
        } else if (nonNull(httpHost) && nonNull(request)) {
            return logTransaction(new URI(request.getRequestLine().getUri()), request.getRequestLine().getMethod());
        } else {
            return null;
        }
    }

    private Transaction logTransaction(URI uri, String method) {
        return Cat.newTransaction(HTTP_CLIENT.name(), method + " - " + uri.getScheme() + "://" + uri.getAuthority() + getConcreteUri(uri.getPath()));
    }

    private String getConcreteUri(String uri) {
        int index;
        if ((index = uri.indexOf(';')) > -1) {
            uri = uri.substring(0, index);
        }
        return uri;
    }
}
