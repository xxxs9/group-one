package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.CommentService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author L-YX
 * @version 1.0
 * @description
 * @data 2019-06-18 14:48
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 后台评论列表
     */
    @GetMapping("/list")
    public JSONObject listUser(HttpServletRequest request) {
        return commentService.listAllComment(CommonUtil.request2Json(request));
    }

    /**
     * 后台移除评论
     */
    @PostMapping("/removeComment")
    public JSONObject removeComment(@RequestBody JSONObject requestJson){
        return commentService.removeComment(requestJson);
    }

    /**
     * 后台评论详情
     */
    @GetMapping("/commentDetails")
    public JSONObject commentDetails(HttpServletRequest request){
        return commentService.getByPostId(CommonUtil.request2Json(request));
    }
}
