package com.yq.starter.cat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
@ConfigurationProperties(prefix = "fw.cat")
@Data
public class CatProperties {

    /**
     * 服务器列表,格式为ip:httpPort:port,如127.0.0.1:8080:2080
     */

    private Set<String> servers;

    /**
     * 是否开启,默认true
     */
    private boolean enable = true;

    /**
     * 不记录的uri列表
     */
    private Set<String> ignoreUris = new HashSet<>();
}
