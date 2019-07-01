package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ExternalUserDao;
import com.heeexy.example.service.ExternalUserService;
import com.heeexy.example.service.PostService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: liminhao
 * @date: 2019-06-28 10:16
 * @description: 前台用户接口
 * @version:
 */
@RestController
@RequestMapping("/api/user")
public class ApiExternalUserController {

    @Autowired
    ExternalUserService externalUserService;

    /**
     * 获取用户信息(发帖数量，关注数量，粉丝数量，点赞数量，收藏数量，头像、昵称)
     * @param requestJson
     * @return
     */
    @PostMapping("/myself")
    public JSONObject getMyself(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson, "uuId");
        return externalUserService.getMyself(requestJson);
    }

    @PostMapping("/others")
    public JSONObject getOthers(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson, "uuId");
        return externalUserService.getOthers(requestJson);
    }





}

