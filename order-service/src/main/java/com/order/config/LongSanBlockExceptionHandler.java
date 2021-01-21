package com.order.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: LongSanBlockExceptionHandler.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/01/21 15:18
 */
@Component
public class LongSanBlockExceptionHandler implements BlockExceptionHandler {

    /**
     * 配置sentinel各种熔断降级后的异常返回
     */

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        Map<String, String> map = new HashMap<>();
        if (e instanceof FlowException) {
            map.put("code", "1");
            map.put("msg", "限流异常");
        }if (e instanceof DegradeException) {
            map.put("code", "2");
            map.put("msg", "降级异常");
        }if (e instanceof ParamFlowException) {
            map.put("code", "3");
            map.put("msg", "热点参数异常");
        }if (e instanceof SystemBlockException) {
            map.put("code", "4");
            map.put("msg", "系统异常");
        }if (e instanceof AuthorityException) {
            map.put("code", "5");
            map.put("msg", "授权异常");
        }

        httpServletResponse.setStatus(200);
        httpServletResponse.setHeader("content-type","application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSONObject.toJSONString(map));
    }
}