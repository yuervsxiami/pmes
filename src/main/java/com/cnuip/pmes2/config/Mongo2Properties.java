package com.cnuip.pmes2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/20.
 * Time: 15:17
 */
@ConfigurationProperties(prefix = "spring.data.mongodb2")
@Getter
@Setter
public class Mongo2Properties {
    private String host;
    private int port;
    private String database;
    private int connectTimeout;
}
