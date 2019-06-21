package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.ExternalUserService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: liminhao
 * @date: 2019-06-18 13:53
 * @description:
 * @version:
 */
@RestController
@RequestMapping("/euser")
public class ExternalUserController {

    @Autowired
    ExternalUserService service;

    @GetMapping("/userlist")
    public JSONObject listUser(HttpServletRequest request) {
        return service.getUser(CommonUtil.request2Json(request));
    }

    @PostMapping("/updateFO")
    public JSONObject updateFansOffset(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"fansOffset");
        return service.updateFansOffset(requestJson);
    }

    @GetMapping("/permlist")
    public JSONObject listPerm(HttpServletRequest request){
        return service.getUserPermission(CommonUtil.request2Json(request));
    }

    @PostMapping("/updatePerm")
    public JSONObject updatePermission(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"uuId,epermissionList");
        return service.removePermission(requestJson);
    }
    @PostMapping("/refreshPerm")
    public JSONObject refreshPermission(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"uuId");
        return service.refreshPermissionStatus(requestJson);
    }
}

