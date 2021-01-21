package com.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Copyright (C), 2006-2010, ChengDu longsan info. Co., Ltd.
 * FileName: com.user.UserApplication.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/01/18 18:42
 */
@SpringBootApplication
public class UserApplication {
    private final static Logger logger = LoggerFactory.getLogger(UserApplication.class);

    public static void main(String[] args){
            SpringApplication.run(UserApplication.class, args);
        }
}