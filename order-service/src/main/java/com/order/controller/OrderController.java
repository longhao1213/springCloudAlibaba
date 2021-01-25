package com.order.controller;

import com.domain.Video;
import com.domain.VideoOrder;
import com.order.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: OrderController.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/01/19 16:00
 */
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    private final static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VideoService videoService;

    int count = 0;

    @RequestMapping("/save")
    public Object save(int videoId) {
//        Video video = restTemplate.getForObject("http://localhost:9000/api/v1/video/find_by_id?videoId=" + videoId, Video.class);
        Video video = videoService.findById(videoId);

        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setVideoId(video.getId());
        videoOrder.setVideoTitle(video.getTitle());
        videoOrder.setCreateTime(new Date());
        return videoOrder;
    }

    @RequestMapping("list")
    public Object list() {

        count++;
//        if (count % 3 == 0) {
//            throw new RuntimeException();
//        }

        Map<String, String> map = new HashMap<>();
        map.put("名字", "longsan");
        map.put("age", "22");
        return map;
    }
}