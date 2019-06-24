package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.impl.StickServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public JSONObject listStick(HttpServletRequest request) {
        JSONObject jsonObject = stickService.listStick(CommonUtil.request2Json(request));
        return stickService.listStick(CommonUtil.request2Json(request));
    }

    @PostMapping("/listStickByPostId")
    public JSONObject listStickByPostId(HttpServletRequest request) {
        return stickService.listStickByPostId(CommonUtil.request2Json(request));
    }

    @PostMapping("/updatePostStick")
    public JSONObject updatePostStick(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "");
        return stickService.updatePostStick(requestJson);
    }
}
