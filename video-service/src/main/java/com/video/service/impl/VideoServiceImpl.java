package com.video.service.impl;

import com.domain.Video;
import com.video.dao.VideoMapper;
import com.video.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: VideoServiceImpl.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/01/19 14:19
 */
@Service
public class VideoServiceImpl implements VideoService {
    private final static Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public Video findVideoById(int videoId) {
        return videoMapper.findVideoById(videoId);
    }
}