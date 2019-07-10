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
@RequestMapping("/api")
public class ApiCommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 前台谁评论过我接口
     * @param requestJson (key:acceptUserId)
     * @return JSONObject
     */
    @PostMapping("/whocomments/whocommentsme")
    public JSONObject AcceptUser(@RequestBody JSONObject requestJson) {
        return commentService.getByAcceptUserId(requestJson);
    }

    /**
     * 前台我评论过谁接口
     * @param requestJson (key:commentUserId)
     * @return JSONObject
     */
    @PostMapping("/mycomments/mycomments")
    public JSONObject CommentUser(@RequestBody JSONObject requestJson) {
        return commentService.getByCommentUserId(requestJson);
    }

    /**
     * 前台添加评论接口
     * @param requestJson (key:postId,postUserId,commentUserId,acceptUserId,commentText)
     * @return JSONObject
     */
    @PostMapping("/comments/comments")
    public JSONObject addComment(@RequestBody JSONObject requestJson){
        Object acceptUserId = requestJson.get("acceptUserId");
        if(acceptUserId == null) {
            requestJson.put("acceptUserId", 0);
        }
        CommonUtil.hasAllRequired(requestJson, "commentText");
        return commentService.addComment(requestJson);
    }

    /**
     * 前台评论详情接口
     * @param requestJson (key:postId)
     * @return JSONObject
     */
    @PostMapping("/detailsCom")
    public JSONObject commentDetails(@RequestBody JSONObject requestJson){
        return commentService.getByPostId(requestJson);
    }

    /**
     * 前台删除评论接口
     * @param requestJson (key:commentId)
     * @return JSONObject
     */
    @PostMapping("/deletecomments/deletecomments")
    public JSONObject removeComment(@RequestBody JSONObject requestJson){
        return commentService.removeComment(requestJson);
    }
}
