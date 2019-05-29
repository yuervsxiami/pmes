package com.cnuip.pmes2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/20.
 * Time: 15:15
 */
@ConfigurationProperties(prefix = "spring.data.mongodb1")
@Getter
@Setter
public class Mongo1Properties {
    private String host;
    private int port;
    private String database;
    private int connectTimeout ;
    private int socketTimeout;
    private boolean socketKeepAlive;
}
