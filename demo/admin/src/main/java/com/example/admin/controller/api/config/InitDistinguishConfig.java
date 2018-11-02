package com.example.admin.controller.api.config;

import com.wz.util.DistinguishUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitDistinguishConfig {
    @Autowired
    DistinguishUtils DistinguishUtils;
    @Bean
    public DistinguishUtils init(){
        DistinguishUtils d = new DistinguishUtils();
        d.initImgObejct();
        return d;
    }
}
