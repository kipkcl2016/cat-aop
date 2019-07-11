package com.yq.starter.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.yq.starter.common.WebConsts.*;


/**
 * @author yq
 * @date 2018/10/11
 */
@Slf4j
public class RequestUtil {
    private static final String TREATED_ATTR_KEY = "TREATED_ATTR_KEY";

    private RequestUtil() {
    }

    /**
     * 获得经过反向代理之后的请求端真实IP地址
     *
     * @return
     */
    public static String getCurrentRequestIP() {
        return getCurrentRequest().map(RequestUtil::getRealIpAddress).orElse(null);
    }

    /**
     * 获得经过反向代理之后的请求端真实IP地址
     *
     * @param request
     * @return
     */
    public static String getRealIpAddress(HttpServletRequest request) {
        String[] ipHeaderArray = {X_FORWARDED_FOR, HEADER_KEY_PROXY_CLIENT_IP, HEADER_KEY_WL_PROXY_CLIENT_IP, HEADER_KEY_HTTP_CLIENT_IP, HEADER_KEY_HTTP_X_FORWARDED_FOR, HEADER_KEY_X_REAL_IP};
        String ipAddress;
        for (String ipHeader : ipHeaderArray) {
            ipAddress = request.getHeader(ipHeader);
            if (StringUtils.isNotBlank(ipAddress) && !UNKNOWN.equalsIgnoreCase(ipAddress)) {
                return ipAddress.contains(",") ? ipAddress.split(",")[0] : ipAddress;
            }
        }

        ipAddress = request.getRemoteAddr();

        if (ipAddress.equals(LOCAL_IP_127) || ipAddress.equals(LOCAL_IP_01)) {
            //根据网卡获取本机配置的IP地址
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                log.error("failed to parse localIP", e);
            }
            if (null != inetAddress) {
                ipAddress = inetAddress.getHostAddress();
            }
        }
        return ipAddress.contains(",") ? ipAddress.split(",")[0] : ipAddress;
    }

    /**
     * 从header获param中获得token
     *
     * @param request
     * @return
     */
    public static String getFromHeaderOrParam(HttpServletRequest request, String key) {
        String value = request.getHeader(key);
        if (null == value) {
            value = request.getParameter(key);
        }
        return value;
    }

    /**
     * 获得请求地址全路径
     *
     * @param request
     * @return
     */
    public static String getRequestPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request
                .getRequestURI();
    }

    /**
     * 根据request获得headerMap
     *
     * @param request
     * @return
     */
    public static Map<String, String> getRequestHeaderMap(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    /**
     * 获得当前线程中的request对象
     *
     * @return
     */
    public static Optional<HttpServletRequest> getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(((ServletRequestAttributes) requestAttributes))
                .map(ServletRequestAttributes::getRequest);
    }

    /**
     * 获得当前线程中的请求ID
     *
     * @return
     */
    public static String getCurrentRequestId() {
        Optional<HttpServletRequest> request = getCurrentRequest();
        return request.map(s -> s.getHeader(WebConsts.REQUEST_ID_HEADER_KEY)).orElse(null);
    }

    /**
     * 获得请求ID
     *
     * @return
     */
    public static String getRequestId(HttpServletRequest request) {
        return request.getHeader(WebConsts.REQUEST_ID_HEADER_KEY);
    }

    /**
     * 标记此次请求失败
     * 传递的状态码会写入requestAttribute位置
     * 用以向后续filter传递请求状态
     *
     * @param code
     */
    public static void setFailureInfo(@NonNull HttpServletRequest request, int code, Throwable e) {
        request.setAttribute(WebConsts.REQUEST_FAILURE_CODE, code);
        request.setAttribute(WebConsts.REQUEST_FAILURE_INFO, e);
    }

    /**
     * 获取此次请求的错误码（发生在出现异常或被异常拦截器拦截之后，标记请求失败的状态码通常为9开头）
     */
    public static Integer getFailureCode(@NonNull HttpServletRequest request) {
        return (Integer) request.getAttribute(WebConsts.REQUEST_FAILURE_CODE);
    }

    /**
     * 获取此次请求的错误码（发生在出现异常或被异常拦截器拦截之后，标记请求失败的状态码通常为9开头）
     */
    public static Throwable getFailureInfo(@NonNull HttpServletRequest request) {
        return (Throwable) request.getAttribute(WebConsts.REQUEST_FAILURE_INFO);
    }

    /**
     * 从request获得请求时间标记（attribute位置）
     */
    public static String getRequestTime(@Nonnull HttpServletRequest request) {
        return (String) request.getAttribute(WebConsts.REQUEST_TIME);
    }

    /**
     * 标记请求已经被处理（拦截异常并通过response-writer返回内容）
     *
     * @param request
     */
    public static void markTreated(@Nonnull HttpServletRequest request) {
        request.setAttribute(TREATED_ATTR_KEY, TREATED_ATTR_KEY);
    }

    /**
     * 标记请求已经被处理（拦截异常并通过response-writer返回内容）
     *
     * @param request
     */
    public static boolean isTreated(@Nonnull HttpServletRequest request) {
        return TREATED_ATTR_KEY.equals(request.getAttribute(TREATED_ATTR_KEY));
    }
}
