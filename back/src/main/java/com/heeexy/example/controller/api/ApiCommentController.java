package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.CommentService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author L-YX
 * @version 1.0
 * @description 前台评论接口
 * @data 2019-06-28 09:53
 */
@RestController
@RequestMapping("/api/wxDynamic")
public class ApiCommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 前台谁评论过我
     */
    @GetMapping("/acceptCom")
    public JSONObject AcceptUser(@RequestBody JSONObject requestJson) {
        return commentService.getByAcceptUserId(requestJson);
    }

    /**
     * 前台我评论过谁
     */
    @GetMapping("/userCom")
    public JSONObject CommentUser(@RequestBody JSONObject requestJson) {
        return commentService.getByCommentUserId(requestJson);
    }

    /**
     * 前台添加评论
     */
    @PostMapping("/addCom")
    public JSONObject addComment(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson, "commentText");
        return commentService.addComment(requestJson);
    }

    /**
     * 前台评论详情
     */
    @GetMapping("/detailsCom")
    public JSONObject commentDetails(@RequestBody JSONObject requestJson){
        return commentService.getByPostId(requestJson);
    }

    /**
     * 前台删除评论
     */
    @PostMapping("/delCom")
    public JSONObject removeComment(@RequestBody JSONObject requestJson){
        return commentService.removeComment(requestJson);
    }
}
