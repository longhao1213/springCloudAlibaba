package com.domain;


import lombok.Data;

import java.util.Date;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: User.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/01/19 14:03
 */
@Data
public class User {
    private Integer id;
    private String name;
    private String pwd;
    private String headImg;
    private String phone;
    private Date createTime;
    private String wechat;
}