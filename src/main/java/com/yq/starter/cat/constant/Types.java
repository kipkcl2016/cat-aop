package com.yq.starter.cat.constant;

/**
 * @author yq
 * @date 2019-02-19
 */
public enum Types {
    URI,
    URL,
    REQUEST_METHOD,
    REQUEST_IP,
    METHOD,
    SQL,
    SQL_DATABASE,
    SERVER_ID,
    THREAD,
    /**
     * 使用apache httpClient工具包发起的http请求
     */
    HTTP_CLIENT,
    /**
     * 使用kevinsawicki.github 提供的httpRequest工具包发起的http请求
     */
    HTTP_REQUEST,
    /**
     * 使用okhttp3包发起的http请求
     */
    OK_HTTP,
    /**
     * 使用restTemplate发起的http请求
     */
    REST_TEMPLATE,
    RABBIT_MQ_RECEIVE,
    RABBIT_MQ_SEND,
    ;
}
