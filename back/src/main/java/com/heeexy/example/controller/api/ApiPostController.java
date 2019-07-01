package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.impl.PostServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/28 9:48
 * @description：小程序的接口
 * @version: 0.1$
 */
@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    @Autowired
    PostServiceImpl postService;

    /**
     * 获取帖子详情
     * @param requestJson 帖子ID 当前用户ID
     * @return
     */
    @PostMapping("/postDetail")
    public JSONObject postDetail(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "postId,userId");
        return postService.getDetailById(requestJson);
    }

    @PostMapping("/listPostType")
    public JSONObject listPostType(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "postStick");
        return postService.getStickPost(requestJson);
    }
}
