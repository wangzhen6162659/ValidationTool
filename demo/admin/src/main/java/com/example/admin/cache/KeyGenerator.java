package com.example.admin.cache;

import org.springframework.beans.factory.annotation.Value;

public class KeyGenerator {
    @Value("${spring.redis.key-prefix:dev}")
    private String keyPrefix;
//    @Value("${spring.redis.biz-modular:${spring.application.name:null}}")
//    private String bizModular;

    /**
     * key 前缀
     *
     * @return
     */
    public String keyPrefix() {
        return keyPrefix;
    }

    /**
     * 业务系统
     *
     * @return
     */
//    public String bizModular() {
//        return bizModular;
//    }

}
