package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.PostService;
import com.heeexy.example.service.impl.PostServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/18 10:53
 * @description：帖子的Controller层
 * @version: 1.0$
 */

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostServiceImpl postService;

    @GetMapping("/list")
    public JSONObject listUser(HttpServletRequest request) {
        return postService.listPost(CommonUtil.request2Json(request));
    }
}
