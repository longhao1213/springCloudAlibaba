package com.video.service;

import com.domain.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: VideoService.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/01/19 14:18
 */
public interface VideoService {

    Video findVideoById(int videoId);
}