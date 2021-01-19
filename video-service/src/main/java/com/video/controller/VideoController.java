package com.video.controller;

import com.video.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: VideoController.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/01/19 14:18
 */
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {
    private final static Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Autowired
    private VideoService videoService;


    @RequestMapping("/find_by_id")
    public Object findVideoById(int videoId) {
        return videoService.findVideoById(videoId);
    }

}