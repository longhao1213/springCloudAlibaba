package com.video.dao;

import com.domain.Video;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: VideoMapper.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/01/19 14:18
 */
@Repository
public interface VideoMapper {

    @Select("select * from video where id = #{videoId}")
    Video findVideoById(@Param("videoId")int videoId);
}