package com.cnuip.pmes2.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * MongoDBConfig
 *
 * @author: xiongwei
 * Date: 2018/3/16 上午10:23
 */
@Configuration
@EnableConfigurationProperties(value = {Mongo1Properties.class, Mongo2Properties.class})
public class MongoDBConfig {

    @Bean
    public GridFsTemplate gridFsTemplate(@Qualifier("primaryMongoDbFactory") MongoDbFactory mongoDbFactory, @Qualifier("myMongoConverter")MongoConverter mongoConverter) throws Exception {
        return new GridFsTemplate(mongoDbFactory, mongoConverter);
    }

    @Bean
    public MongoConverter myMongoConverter(@Qualifier("primaryMongoDbFactory") MongoDbFactory mongoDbFactory,MongoMappingContext context) {
        return new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), context);
    }

    @Bean
    public MongoTemplate primaryMongoTemplate(@Qualifier("primaryMongoDbFactory") MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

    @Bean
    public MongoTemplate secondMongoTemplate(@Qualifier("secondMongoDbFactory") MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

    @Bean
    public MongoDbFactory primaryMongoDbFactory(Mongo1Properties mongo1Properties) {
        return new SimpleMongoDbFactory(new MongoClient(new ServerAddress(mongo1Properties.getHost(),mongo1Properties.getPort()), MongoClientOptions.builder().connectTimeout(mongo1Properties.getConnectTimeout()).socketTimeout(mongo1Properties.getSocketTimeout()).socketKeepAlive(mongo1Properties.isSocketKeepAlive()).build()),mongo1Properties.getDatabase());
    }

    @Bean
    public MongoDbFactory secondMongoDbFactory(Mongo2Properties mongo2Properties) {
        return new SimpleMongoDbFactory(new MongoClient(new ServerAddress(mongo2Properties.getHost(),mongo2Properties.getPort()), MongoClientOptions.builder().connectTimeout(mongo2Properties.getConnectTimeout()).build()),mongo2Properties.getDatabase());
    }
}
