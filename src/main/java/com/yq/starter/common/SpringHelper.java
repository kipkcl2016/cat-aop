package com.yq.starter.common;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


/**
 * Created by yq on 2017/3/24.
 * SpringMVC辅助类，用来获得spring管理的示例、相关配置等
 * 大部分方法只能在spring容器初始化完成之后使用
 */
@Component
@Slf4j
public class SpringHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;
    private static Environment environment = null;
    private static String appName = null;
    private static String activeProfile = null;
    private static boolean hasFeignEvn = false;


    /**
     * 获得当前项目的applicationName
     *
     * @return
     */
    public static String getAppName() {
        return appName;
    }

    /**
     * 通过name获取 Bean
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        try {
            return getApplicationContext().getBean(name);
        } catch (BeansException e) {
            log.warn("error occured when try get spring bean, beanName[{}],error msg[{}]", name, e.getMessage());
            return null;
        }
    }

    public static <T> T getFeignClient(String appName, Class<T> tClass) {
        return ((FeignContext) applicationContext.getBean("feignContext")).getInstance(appName, tClass);
    }

    /**
     * 获取applicationContext
     *
     * @return
     */
    public static synchronized ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public synchronized void setApplicationContext(ApplicationContext applicationContext) {
        log.info("init SpringHelper...");
        if (SpringHelper.applicationContext == null) {
            SpringHelper.applicationContext = applicationContext;
        }
        try {
            Class.forName("org.springframework.cloud.openfeign.FeignContext");
            hasFeignEvn = applicationContext.getBean("feignContext") != null;
        } catch (ClassNotFoundException e) {
            hasFeignEvn = false;
        }

        SpringHelper.environment = applicationContext.getBean("environment", Environment.class);
        SpringHelper.appName = SpringHelper.getAppName(SpringHelper.environment);
        SpringHelper.activeProfile = SpringHelper.getActiveProfile(SpringHelper.environment);
        log.info("SpringHelper initialized");
    }

    /**
     * get config "spring.application.name" value
     *
     * @param environment
     * @return
     */
    public static String getAppName(@NonNull Environment environment) {
        return environment.getProperty("spring.application.name");
    }

    /**
     * get the first actived profile
     *
     * @param environment
     * @return
     */
    public static String getActiveProfile(@NonNull Environment environment) {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length == 0) {
            return "default";
        } else {
            return activeProfiles[0];
        }
    }

    /**
     * 通过class获取Bean
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> clazz) {
        try {
            return getApplicationContext().getBean(clazz);
        } catch (BeansException e) {
            log.warn("error occured when try get spring bean, beanClass[{}],error msg[{}]", clazz.getSimpleName(), e
                    .getMessage());
            throw e;
        }
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            return getApplicationContext().getBean(name, clazz);
        } catch (BeansException e) {
            log.warn("error occured when try get spring bean, beanName[{}], beanClass[{}],error msg[{}]", name, clazz
                    .getSimpleName(), e.getMessage());
            return null;
        }
    }

    /**
     * 获得当前项目激活的profile
     */
    public static String getActiveProfile() {
        return activeProfile;
    }

    /**
     * 获取配置在applicationProperties中的系统参数
     *
     * @param key
     * @return
     */
    public static String getApplicationProperties(String key) {
        return environment.getProperty(key);
    }

    /**
     * 按类型获取定义在application中的参数
     *
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getApplicationProperties(String key, Class<T> tClass) {
        return environment.getProperty(key, tClass);
    }

    /**
     * @return 是否拥有Feign环境（有feign依赖库，并且开启配置）
     */
    public static boolean hasFeignEvn() {
        return hasFeignEvn;
    }
}
