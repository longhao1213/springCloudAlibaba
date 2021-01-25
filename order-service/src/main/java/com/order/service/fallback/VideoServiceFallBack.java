package com.order.service.fallback;

import com.domain.Video;
import com.order.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: VideoServiceFallBack.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/01/25 10:01
 */
@Service
public class VideoServiceFallBack implements VideoService {
    @Override
    public Video findById(int videoId) {
        Video video = new Video();
        video.setTitle("服务熔断");
        return video;
    }
}