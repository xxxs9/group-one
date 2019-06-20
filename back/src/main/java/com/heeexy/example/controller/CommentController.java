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

    @PostMapping("/updateComment")
    public JSONObject updateComment(@RequestBody JSONObject requestJson){
        System.out.println(requestJson);
        return commentService.updateComment(requestJson);
    }

    @GetMapping("/list1")
    public JSONObject listcomment(HttpServletRequest request) {
        return commentService.getByAcceptUserId(CommonUtil.request2Json(request));
    }
}
