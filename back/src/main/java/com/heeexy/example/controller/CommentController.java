package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.CommentService;
import com.heeexy.example.service.TemplateService;
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
    @Autowired
    private TemplateService templateService;

    /**
     * 后台评论列表
     * @param request (key:commentText,commentTime,commentUserName)
     * @return JSONObject
     */
    @GetMapping("/list")
    public JSONObject listUser(HttpServletRequest request) {
        return commentService.listAllComment(CommonUtil.request2Json(request));
    }

    /**
     * 后台移除评论
     * @param requestJson (key:commentId)
     * @return JSONObject
     */
    @PostMapping("/removeComment")
    public JSONObject removeComment(@RequestBody JSONObject requestJson){
        if(requestJson.getString("commentState").equals("0")){
            templateService.addWarningTemplate(requestJson);
        }
        return commentService.removeComment(requestJson);
    }

    /**
     * 后台评论详情
     * @param request (key:postId)
     * @return JSONObject
     */
    @GetMapping("/commentDetails")
    public JSONObject commentDetails(HttpServletRequest request){
        return commentService.getByPostId(CommonUtil.request2Json(request));
    }

}
