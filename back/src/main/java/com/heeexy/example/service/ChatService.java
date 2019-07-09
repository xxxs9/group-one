package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
/**
 * @author: linchen
 * @description:消息信息Service接口
 * @date: 2019/7/3
 * @Version:1.0
 */
public interface ChatService {
    /**
     * 私聊发送消息
     * @param jsonObject 接收用户ID 发送内容 当前用户ID
     * @return
     */
    JSONObject addChatMessage (JSONObject jsonObject);
    /**
     * 获取消息列表
     * @param jsonObject 当前用户ID
     * @return 消息列表
     */
    JSONObject getChatList  (JSONObject jsonObject);
    /**
     * 获取消息详情
     * @param jsonObject 接收用户ID ，当前用户ID
     * @return 消息详情
     */

    JSONObject getChatMessage  (JSONObject jsonObject);
    /**
     * 删除消息
     * @param jsonObject 接收用户ID ，当前用户ID
     * @return
     */
    JSONObject deleteChatMessage  (JSONObject jsonObject);
}
