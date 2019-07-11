package com.yq.starter.common;

import java.util.Arrays;
import java.util.List;

/**
 * @author yq
 * @date 2018/12/7
 */
public class WebConsts {
    /**
     * request-header中的version-key
     */
    public static final String VERSION_KEY_HEADER = "version";
    /**
     * request header中的请求链id名
     */
    public static final String REQUEST_ID_HEADER_KEY = "requestid";
    /**
     * token在request中的key值
     */
    public static final String TOKEN_KEY = "token";
    /**
     * sign在request中的key值
     */
    public static final String SIGN_KEY = "sign";
    /**
     * timestamp在request中的key值
     */
    public static final String TIMESTAMP_KEY = "timestamp";
    /**
     * auth2中使用到的Authorization在request中的key值
     */
    public static final String AUTHORIZATION_KEY = "authorization";
    /**
     * 请求失败的状态码，存放在requestAttribute位置，用来向后续filter传递状态
     */
    public static final String REQUEST_FAILURE_CODE = "req-failure-code";
    /**
     * 请求失败时用来存储异常信息，向cat上报时使用
     */
    public static final String REQUEST_FAILURE_INFO = "req-failure-info";
    public static final String HEADER_KEY_X_REAL_IP = "X-Real-IP";
    public static final String HEADER_KEY_PROXY_CLIENT_IP = "Proxy-Client-IP";
    public static final String HEADER_KEY_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    public static final String HEADER_KEY_HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    public static final String HEADER_KEY_HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    /**
     * 请求开始时间，标记应用接收到请求的时间
     */
    public static final String REQUEST_TIME = "reqTime";
    /**
     * unkonw str
     */
    public static final String UNKNOWN = "unknown";

    /**
     * 本地IP格式，用作比较
     */
    public static final String LOCAL_IP_127 = "127.0.0.1";
    /**
     * 本地IP格式，用作比较
     */
    public static final String LOCAL_IP_01 = "0:0:0:0:0:0:0:1";
    /**
     * 发生内部调用时需要被传递的request headerNames
     */
    public static final List<String> SHOULD_TRANSFER_HEADERS = Arrays
            .asList(REQUEST_ID_HEADER_KEY, AUTHORIZATION_KEY, TOKEN_KEY, SIGN_KEY, TIMESTAMP_KEY, VERSION_KEY_HEADER, VERSION_KEY_HEADER
                    , X_FORWARDED_FOR, HEADER_KEY_PROXY_CLIENT_IP, HEADER_KEY_WL_PROXY_CLIENT_IP, HEADER_KEY_HTTP_CLIENT_IP
                    , HEADER_KEY_HTTP_X_FORWARDED_FOR, HEADER_KEY_X_REAL_IP);

    /**
     * swagger相关访问地址前缀,用以过滤匹配
     */
    public static final List<String> SWAGGER_URI_PREFIXS = Arrays
            .asList("/webjars/", "/swagger-ui.html", "/csrf", "/swagger-resources", "/v2/api-docs");

    /**
     * 静态资源后缀名
     */
    public static final List<String> STATIC_URI_SUFFIXS = Arrays
            .asList(".js", ".css", ".jpg", ".gif", ".png", ".ico");

    private WebConsts() {
    }
}
