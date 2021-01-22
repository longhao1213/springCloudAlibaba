package com.order.service;

import com.domain.Video;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: VideoService.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/01/21 10:15
 */
@FeignClient(value = "video-service",fallback = VideoService.VideoServiceFallBack.class)
public interface VideoService {

    @GetMapping("/api/v1/video/find_by_id")
    Video findById(@RequestParam("videoId") int videoId);

    class VideoServiceFallBack implements VideoService {
        @Override
        public Video findById(int videoId) {
            Video video = new Video();
            video.setTitle("服务熔断");
            return video;
        }
    }

}