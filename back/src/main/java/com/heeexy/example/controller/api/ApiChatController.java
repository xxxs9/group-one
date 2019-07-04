package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.ChatService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: linchen
 * @description:
 * @date: 2019/7/3
 * @Version:
 */
@RestController
@RequestMapping("/api/chat")
public class ApiChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/addChatMessage")
    public JSONObject getThumbsUp(@RequestBody JSONObject requestJson) {
//        CommonUtil.hasAllRequired(requestJson, "postId,userId");
        return chatService.addChatMessage(requestJson);
    }
    @PostMapping("/getChatList")
    public JSONObject getChatList(@RequestBody JSONObject requestJson) {
//        CommonUtil.hasAllRequired(requestJson, "postId,userId");
        return chatService.getChatList(requestJson);
    }
    @PostMapping("/getChatMessage")
    public JSONObject getChatMessage(@RequestBody JSONObject requestJson) {
//        CommonUtil.hasAllRequired(requestJson, "postId,userId");
        return chatService.getChatMessage(requestJson);
    }
}
