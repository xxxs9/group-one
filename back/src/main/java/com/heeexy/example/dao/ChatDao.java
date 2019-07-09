package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
/**
 * @author: linchen
 * @description:消息Dao层
 * @date: 2019/7/2
 * @Version:1.0
 */
public interface ChatDao {
    /**
     * 添加私聊发送消息
     * @param jsonObject 接收用户ID 发送内容 当前用户ID
     * @return
     */
    int addFirstChatMessage (JSONObject jsonObject);
    /**
     * 添加私聊发送消息
     * @param jsonObject 接收用户ID 发送内容 当前用户ID
     * @return
     */
    int addSecondChatMessage (JSONObject jsonObject);
    /**
     * 修改消息已读状态
     * @param jsonObject 接收用户ID 当前用户ID
     * @return
     */
    int updateChatMessageStatus (JSONObject jsonObject);
    /**
     * 删除消息
     * @param jsonObject 接收用户ID 当前用户ID
     * @return
     */
    int deleteChatMessage (JSONObject jsonObject);
    /**
     * 获取消息界面详情信息
     * @param jsonObject 当前用户ID
     * @return 用户头像 发送消息 时间 用户名
     */
    List<JSONObject> getUserById (JSONObject jsonObject);
    /**
     * 获取消息详情信息
     * @param jsonObject 当前用户ID 接收用户ID
     * @return 状态（0,1） 用户头像 发送消息 时间
     */
    List<JSONObject> getChatMessage (JSONObject jsonObject);


}
