package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.impl.StickServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/21 21:59
 * @description：板块的Controller层
 * @version: 0.1$
 */
@RestController
@RequestMapping("/stick")
public class StickController {

    @Autowired
    private StickServiceImpl stickService;

    @GetMapping("/list")
    public JSONObject listSort(HttpServletRequest request) {
        return stickService.listStick(CommonUtil.request2Json(request));
    }
}
