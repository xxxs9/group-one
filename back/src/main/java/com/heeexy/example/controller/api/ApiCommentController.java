package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.CommentService;
import com.heeexy.example.service.PostService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author L-YX
 * @version 1.0
 * @description
 * @data 2019-06-28 09:53
 */
@RestController
@RequestMapping("/wxDynamic")
public class ApiCommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    /**
     * 前台谁评论过我
     */
    @GetMapping("/acceptCom")
    public JSONObject AcceptUser(HttpServletRequest request) {
        return commentService.getByAcceptUserId(CommonUtil.request2Json(request));
    }

    /**
     * 前台我评论过谁
     */
    @GetMapping("/userCom")
    public JSONObject CommentUser(HttpServletRequest request) {
        return commentService.getByCommentUserId(CommonUtil.request2Json(request));
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
    public JSONObject commentDetails(HttpServletRequest request){
        return commentService.getByPostId(CommonUtil.request2Json(request));
    }

    /**
     * 前台删除评论
     */
    @PostMapping("/delCom")
    public JSONObject removeComment(@RequestBody JSONObject requestJson){
        return commentService.removeComment(requestJson);
    }
}
