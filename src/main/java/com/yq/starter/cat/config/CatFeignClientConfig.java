package com.yq.starter.cat.config;

import com.dianping.cat.Cat;
import com.yq.starter.cat.constant.Types;
import com.yq.starter.cat.context.CatContextImpl;
import com.yq.starter.common.RequestUtil;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.dianping.cat.Cat.Context.*;
import static com.yq.starter.common.WebConsts.SHOULD_TRANSFER_HEADERS;

/**
 * @author yq
 * @date 2018/10/10
 */
@Component
@ConditionalOnClass(RequestInterceptor.class)
public class CatFeignClientConfig {

    @Bean
    public RequestInterceptor headerInterceptor() {
        return requestTemplate ->
                RequestUtil.getCurrentRequest().ifPresent(r -> {
                    CatContextImpl catContext = new CatContextImpl();
                    Cat.logEvent(Types.URL.name(), requestTemplate.url());
                    Cat.logRemoteCallClient(catContext, Cat.getManager().getDomain());
                    requestTemplate.header(ROOT, catContext.getProperty(ROOT));
                    requestTemplate.header(PARENT, catContext.getProperty(PARENT));
                    requestTemplate.header(CHILD, catContext.getProperty(CHILD));
                    SHOULD_TRANSFER_HEADERS.forEach(k -> requestTemplate.header(k, r.getHeader(k)));
                });
    }
}
