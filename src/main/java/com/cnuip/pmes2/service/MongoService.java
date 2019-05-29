package com.cnuip.pmes2.service;

import java.util.List;

/**
 * MongoService
 *
 * @author: xiongwei
 * Date: 2018/3/16 上午10:31
 */
public interface MongoService {

    /**
     * 根据SYSID从mongodb读取xml文件内容
     * @param sysId
     * @return
     */
    String readXmlContentBySysId(String sysId);
}
