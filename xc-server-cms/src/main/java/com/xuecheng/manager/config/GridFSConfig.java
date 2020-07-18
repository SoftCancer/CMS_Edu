package com.xuecheng.manager.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GridFS 是MongoDB 提供的用于持久化存储文件的模块。
 * @Description: 配置 GridFSBucket 类 用于打开下载流，
 * 通过 GridFS 下载MongoDB中的文件。
 * @author: YaoGuangXun
 * @date: 2020/7/11 23:04
 * @Version: 1.0
 */
@Configuration
public class GridFSConfig {

    @Value("${spring.data.mongodb.database}")
    String db;

    @Bean
    public GridFSBucket getGridFSBucket(MongoClient mongoClient){
        MongoDatabase database = mongoClient.getDatabase(db);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        return gridFSBucket;
    }
}
