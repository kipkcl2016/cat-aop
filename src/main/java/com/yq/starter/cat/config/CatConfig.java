package com.yq.starter.cat.config;

import com.dianping.cat.configuration.ClientConfigProvider;
import com.dianping.cat.configuration.client.entity.ClientConfig;
import com.dianping.cat.configuration.client.entity.Server;
import com.yq.starter.common.SpringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * @author yq
 * @date 2019-01-24
 */
@Slf4j
public class CatConfig implements ClientConfigProvider {
    private static boolean aopEffective = true;

    public static void setAopEffective(boolean b){
        aopEffective = b;
    }

    public static boolean getAopEffective(){
        return aopEffective;
    }

    @Override
    public ClientConfig getClientConfig() {
        CatProperties catProperties = SpringHelper.getBean(CatProperties.class);
        ClientConfig clientConfig = new ClientConfig();
        for (String server : catProperties.getServers()) {
            String[] params = server.split(":");
            Assert.isTrue(params.length == 3, "cat server配置格式有误");
            clientConfig.addServer(new Server(params[0]).setHttpPort(Integer.parseInt(params[1]))
                    .setPort(Integer.parseInt(params[2])));
        }
        clientConfig.setDomain(SpringHelper.getAppName() + "-" + SpringHelper.getActiveProfile());
        CatConfig.setAopEffective(catProperties.isEnable());
        log.info("CatConfig initiated.");
        return clientConfig;
    }
}
