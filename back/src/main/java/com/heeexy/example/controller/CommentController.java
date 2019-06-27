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


    @GetMapping("/list")
    public JSONObject listUser(HttpServletRequest request) {
        return commentService.listAllComment(CommonUtil.request2Json(request));
    }

    /**
     * 移除评论
     */
    @PostMapping("/removeComment")
    public JSONObject removeComment(@RequestBody JSONObject requestJson){
        return commentService.removeComment(requestJson);
    }

    /**
     * 谁评论过我
     */
    @GetMapping("/AcceptUser")
    public JSONObject AcceptUser(HttpServletRequest request) {
        return commentService.getByAcceptUserId(CommonUtil.request2Json(request));
    }

    /**
     * 我评论过谁
     */
    @GetMapping("/CommentUser")
    public JSONObject CommentUser(HttpServletRequest request) {
        return commentService.getByCommentUserId(CommonUtil.request2Json(request));
    }

    /**
     * 评论详情
     */
    @GetMapping("/commentDetails")
    public JSONObject commentDetails(HttpServletRequest request){
        return commentService.getByPostId(CommonUtil.request2Json(request));
    }

    /**
     * 添加评论
     */
    @PostMapping("/addComment")
    public JSONObject addComment(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson, "commentText");
        return commentService.addComment(requestJson);
    }
}
