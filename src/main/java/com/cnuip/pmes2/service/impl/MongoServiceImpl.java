package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.service.MongoService;
import com.mongodb.gridfs.GridFSDBFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * MongoServiceImpl
 *
 * @author: xiongwei
 * Date: 2018/3/16 上午10:32
 */
@Service
public class MongoServiceImpl implements MongoService {

    private final Logger logger = LoggerFactory.getLogger(MongoServiceImpl.class);

    @Autowired
    private GridFsOperations gridFsOperations;

    @Override
    public String readXmlContentBySysId(String sysId) {

        GridFSDBFile file = this.gridFsOperations.findOne(new Query().addCriteria(Criteria.where(
                "_id").is(sysId)));
        String content = null;
        if (file != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                file.writeTo(bos);
                content = new String(bos.toByteArray());
            } catch (IOException e) {
                logger.error("读取【sysId=" + sysId + "】xml文件内容出错:", e);
            }
        }
        return content;
    }
}
