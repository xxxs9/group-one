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
 * @description:小程序的消息信息接口
 * @date: 2019/7/3
 * @Version:1.0
 */
@RestController
@RequestMapping("/api/message")
public class ApiChatController {

    @Autowired
    private ChatService chatService;

    /**
     * 私聊发送消息
     * @param requestJson 接收用户ID 发送内容 当前用户ID
     * @return
     */
    @PostMapping("/seedmessage")
    public JSONObject getThumbsUp(@RequestBody JSONObject requestJson) {
//        CommonUtil.hasAllRequired(requestJson, "postId,userId");
        return chatService.addChatMessage(requestJson);
    }

    /**
     * 获取消息列表
     * @param requestJson  当前用户ID
     * @return
     */
    @PostMapping("/getmessage")
    public JSONObject getChatList(@RequestBody JSONObject requestJson) {
//        CommonUtil.hasAllRequired(requestJson, "postId,userId");
        return chatService.getChatList(requestJson);
    }

    /**
     * 获取消息详情
     * @param requestJson  当前用户ID 接收用户ID
     * @return
     */
    @PostMapping("/getmessagedetail")
    public JSONObject getChatMessage(@RequestBody JSONObject requestJson) {
//        CommonUtil.hasAllRequired(requestJson, "postId,userId");
        return chatService.getChatMessage(requestJson);
    }

    /**
     * 删除消息
     * @param requestJson  当前用户ID 接收用户ID
     * @return
     */
    @PostMapping("/deletemessage")
    public JSONObject deleteChatMessage(@RequestBody JSONObject requestJson) {
//        CommonUtil.hasAllRequired(requestJson, "postId,userId");

        return chatService.deleteChatMessage(requestJson);
    }
}
