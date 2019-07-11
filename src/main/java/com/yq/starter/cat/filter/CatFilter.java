package com.yq.starter.cat.filter;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.yq.starter.cat.config.CatProperties;
import com.yq.starter.cat.constant.Types;
import com.yq.starter.cat.context.CatContextImpl;
import com.yq.starter.common.RequestUtil;
import com.yq.starter.common.SpringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.dianping.cat.Cat.Context.*;
import static com.yq.starter.common.WebConsts.*;

/**
 * @author yq
 * @date 2019-01-24
 * 目前使用Cat自带filter，此类留着备用
 */
@Component
@ConditionalOnProperty(name = "fw.cat.enable", havingValue = "true")
public class CatFilter extends OncePerRequestFilter {
    @Autowired
    private CatProperties catProperties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        return catProperties.getIgnoreUris().contains(uri) || SWAGGER_URI_PREFIXS.stream().anyMatch(request.getRequestURI()::startsWith)
                || STATIC_URI_SUFFIXS.stream().anyMatch(request.getRequestURI()::endsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (null != request.getHeader(ROOT)) {
            CatContextImpl catContext = new CatContextImpl();
            catContext.addProperty(ROOT, request.getHeader(ROOT));
            catContext.addProperty(PARENT, request.getHeader(PARENT));
            catContext.addProperty(CHILD, request.getHeader(CHILD));
            Cat.logRemoteCallServer(catContext);
        }
        Transaction t = Cat.newTransaction(Types.URI.name(), request.getRequestURI());
        try {
            Cat.logEvent(Types.SERVER_ID.name(), SpringHelper.getAppName());
            Cat.logEvent(Types.REQUEST_METHOD.name(), request.getMethod(), Message.SUCCESS, request.getRequestURL().toString());
            Cat.logEvent(Types.REQUEST_IP.name(), RequestUtil.getRealIpAddress(request));
            filterChain.doFilter(request, response);
            Integer failureCode = RequestUtil.getFailureCode(request);
            if (null == failureCode) {
                t.setSuccessStatus();
            } else {
                Throwable err = RequestUtil.getFailureInfo(request);
                t.setStatus(err);
                t.addData("errorCode", failureCode.toString());
                t.addData(REQUEST_ID_HEADER_KEY, RequestUtil.getRequestId(request));
                t.addData(REQUEST_TIME, RequestUtil.getRequestTime(request));
                Cat.logError(err);
            }
        } catch (Exception e) {
            t.setStatus(e);
            throw e;
        } finally {
            t.complete();
        }

    }
}
