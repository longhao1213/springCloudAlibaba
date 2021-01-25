package com.video;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: com.video.VideoApplication.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/01/19 14:21
 */
@SpringBootApplication
@MapperScan("com.video.dao")
@EnableDiscoveryClient
public class VideoApplication {
    private final static Logger logger = LoggerFactory.getLogger(VideoApplication.class);

    public static void main(String[] args){
            SpringApplication.run(VideoApplication.class, args);
        }
}