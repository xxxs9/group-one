package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.UserAttentionService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: liminhao
 * @date: 2019-06-28 15:20
 * @description:
 * @version:
 */
@RestController
@RequestMapping("/api/attention")
public class ApiAttentionController {

    @Autowired
    UserAttentionService userAttentionService;

    /**
     * 获取当前用户的粉丝信息
     * @param request
     * @return JSONObject
     */
    @GetMapping("/myfans")
    public JSONObject getFansList(HttpServletRequest request){

        return userAttentionService.getFans(CommonUtil.request2Json(request));
    }

    /**
     * 获取当用户的关注用户信息
     * @param request
     * @return JSONObject
     */
    @GetMapping("/myidols")
    public JSONObject getIdolList(HttpServletRequest request){

        return userAttentionService.getIdol(CommonUtil.request2Json(request));
    }

}

