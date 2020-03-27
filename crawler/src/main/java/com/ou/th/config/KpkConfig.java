package com.ou.th.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-27 23:26
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "kpk")
public class KpkConfig {
    private List<String> mercariUrls;

    @NestedConfigurationProperty
    private KpkProxy proxy;

    @Data
    public static class KpkProxy {
        private String host;
        private Integer port;
    }
}
