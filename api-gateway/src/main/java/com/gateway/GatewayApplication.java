package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: GatewayApplication.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/01/25 09:40
 */

/**
 * 此处加上的多余配置((exclude = DataSourceAutoConfiguration.class) ) 是用于排除父工程中有mybatis相关依赖的自动配置
 * 也可以直接在父工程中去除掉mybatis相关依赖
 * 但是我这里是想偷懒,不想把mybatis的依赖 在每一个子服务中写一份
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GatewayApplication {

    public static void main(String[] args){
            SpringApplication.run(GatewayApplication.class, args);
        }
}