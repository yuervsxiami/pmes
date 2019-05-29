package com.cnuip.pmes2.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * MyBatisConfig
 *
 * @author: xiongwei
 * Date: 2017/12/23 上午11:37
 */
@Configuration
public class MyBatisConfig {

    @Configuration
    @MapperScan(basePackages = {"com.cnuip.pmes2.repository.core"}, sqlSessionFactoryRef = "coreSqlSessionFactory")
    public static class CoreConfig {

        private Logger logger = LoggerFactory.getLogger(CoreConfig.class);

        @Autowired
        @Qualifier("coreDataSource")
        private DataSource dataSource;

        @Primary
        @Bean
        public SqlSessionFactory coreSqlSessionFactory() throws Exception {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setTypeAliasesPackage("com.cnuip.pmes2.domain.core");
            // Configuration
            org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
            configuration.setMapUnderscoreToCamelCase(true);
//            configuration.setLogImpl(StdOutImpl.class); // TODO: 测试用，正式发布时移除此配置
            factoryBean.setConfiguration(configuration);
            // 分页插件
            PageInterceptor pageInterceptor = new PageInterceptor();
            Properties properties = new Properties();
            properties.setProperty("reasonable", "true");
            properties.setProperty("helperDialect", "mysql");
            properties.setProperty("supportMethodsArguments", "true");
            properties.setProperty("autoRuntimeDialect", "true");
            pageInterceptor.setProperties(properties);
            factoryBean.setPlugins(new Interceptor[]{pageInterceptor});
            try {
                ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                factoryBean.setMapperLocations(resolver.getResources("classpath:mappers/core/*.xml"));
                return factoryBean.getObject();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

        @Primary
        @Bean
        public SqlSessionTemplate coreSqlSessionTemplate() throws Exception {
            SqlSessionTemplate template = new SqlSessionTemplate(coreSqlSessionFactory());
            return template;
        }
    }

    @Configuration
    @MapperScan(basePackages = {"com.cnuip.pmes2.repository.inventory"}, sqlSessionFactoryRef = "inventorySqlSessionFactory")
    public static class InventoryConfig {

        private Logger logger = LoggerFactory.getLogger(InventoryConfig.class);

        @Autowired
        @Qualifier("inventoryDataSource")
        private DataSource dataSource;

        @Bean
        public SqlSessionFactory inventorySqlSessionFactory() throws Exception {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setTypeAliasesPackage("com.cnuip.pmes2.domain.inventory");
            // 分页插件
            PageInterceptor pageInterceptor = new PageInterceptor();
            Properties properties = new Properties();
            properties.setProperty("reasonable", "true");
            properties.setProperty("helperDialect", "mysql");
            properties.setProperty("supportMethodsArguments", "true");
            properties.setProperty("autoRuntimeDialect", "true");
            pageInterceptor.setProperties(properties);
            factoryBean.setPlugins(new Interceptor[]{pageInterceptor});
            try {
                ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                factoryBean.setMapperLocations(resolver.getResources("classpath:mappers/inventory/*.xml"));
                return factoryBean.getObject();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

        @Bean
        public SqlSessionTemplate inventorySqlSessionTemplate() throws Exception {
            SqlSessionTemplate template = new SqlSessionTemplate(inventorySqlSessionFactory());
            return template;
        }
    }

}
