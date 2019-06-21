package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.PostService;
import com.heeexy.example.service.impl.PostServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 修改点赞数量
     * @param
     * @return
     */
    @PostMapping("/updateLikeOffset")
    public JSONObject updateLikeOffset(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "likeOffset,postId");
        return postService.updateLikeOffset(requestJson);
    }

    @PostMapping("/updateBrowseOffset")
    public JSONObject updateBrowseOffset(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "browseOffset,postId");
        return postService.updateBrowseOffset(requestJson);
    }

    @PostMapping("/updatePostState")
    public JSONObject updatePostState(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "postState,postId");
        return postService.updatePostState(requestJson);
    }

    @PostMapping("/queryPostById")
    public JSONObject queryPostById(HttpServletRequest request) {
        JSONObject requestJson = CommonUtil.request2Json(request);
        CommonUtil.hasAllRequired(requestJson, "");
        return postService.queryPostById(requestJson);
    }
}
