package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.UserAttentionService;
import com.heeexy.example.util.CommonUtil;
import net.sf.json.processors.JsDateJsonBeanProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
     * @param requestJson
     * @return JSONObject
     */
    @PostMapping("/myfans")
    public JSONObject getFansList(@RequestBody JSONObject requestJson){
        requestJson.put("uuId",requestJson.getInteger("userId"));
        return userAttentionService.getFans(requestJson);
    }

    /**
     * 获取当用户的关注用户信息
     * @param requestJson
     * @return JSONObject
     */
    @PostMapping("/myidols")
    public JSONObject getIdolList(@RequestBody JSONObject requestJson){
        requestJson.put("uuId",requestJson.getInteger("userId"));
        return userAttentionService.getIdol(requestJson);
    }

    /**
     * 新增用户关注
     * @param requestJson
     * @return
     */
    @PostMapping("/attention")
    public JSONObject addAttention(@RequestBody JSONObject requestJson){
        requestJson.put("uuId",requestJson.getInteger("userId"));
        return userAttentionService.addAttention(requestJson);
    }

    /**
     * 取消用户关注
     * @param requestJson
     * @return
     */
    @PostMapping("/removeattention")
    public JSONObject removeAttention(@RequestBody JSONObject requestJson){
        requestJson.put("uuId",requestJson.getInteger("userId"));
        return userAttentionService.removeAttention(requestJson);
    }

}

