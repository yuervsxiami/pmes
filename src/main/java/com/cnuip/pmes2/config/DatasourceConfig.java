package com.cnuip.pmes2.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * DatasourceConfig
 *
 * @author: xiongwei
 * Date: 2017/12/23 上午11:27
 */
@Configuration
@EnableTransactionManagement
public class DatasourceConfig {

    @Primary
    @Bean( name = "coreDataSource")
    @ConfigurationProperties("spring.datasource.druid.core")
    public DataSource coreDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean( name = "inventoryDataSource")
    @ConfigurationProperties("spring.datasource.druid.inventory")
    public DataSource inventoryDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(coreDataSource());
    }
}
