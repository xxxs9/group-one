package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ChatDao;
import com.heeexy.example.service.ChatService;
import com.heeexy.example.service.ExternalUserService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.StringTools;
import com.heeexy.example.util.constants.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author: linchen
 * @description:消息imp层
 * @date: 2019/7/2
 * @Version:1.0
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatDao chatDao;
    @Autowired
    private ExternalUserService externalUserService;
    /**
     * 私聊发送消息
     * @param jsonObject 接收用户ID 发送内容 当前用户ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addChatMessage(JSONObject jsonObject) {
        String firstId = (String)jsonObject.get("firstId");
        Integer uuId = Integer.valueOf(firstId);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("uuId",uuId);

        if(!externalUserService.isHasChatPerm(jsonObject1)){
            return CommonUtil.errorJson(ErrorEnum.E_10020 );
        }
        String secondId = (String)jsonObject.get("secondId");
        Integer i = Integer.valueOf(firstId);
        Integer j = Integer.valueOf(secondId);
        if (i<j) {
            chatDao.addFirstChatMessage(jsonObject);
            chatDao.addSecondChatMessage(jsonObject);
        }else {
            chatDao.addFirstChatMessage(jsonObject);
            chatDao.addSecondChatMessage(jsonObject);
        }

        return  CommonUtil.successJson();
    }
    /**
     * 获取消息列表
     * @param jsonObject 当前用户ID
     * @return 消息列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject getChatList(JSONObject jsonObject) {
        List<JSONObject> userById = chatDao.getUserById(jsonObject);
        for (JSONObject object : userById) {
            Date time =  object.getDate("time");
            String s = StringTools.differentDaysByMillisecond(time);
            object.put("time",s);
        }
        return CommonUtil.successJson(userById);
    }
    /**
     * 获取消息详情
     * @param jsonObject 接收用户ID ，当前用户ID
     * @return 消息详情
     */
    @Override
    public JSONObject getChatMessage(JSONObject jsonObject) {
        chatDao.updateChatMessageStatus(jsonObject);
        List<JSONObject> chatMessage = chatDao.getChatMessage(jsonObject);
        return CommonUtil.successJson(chatMessage);
    }

    /**
     * 删除消息
     * @param jsonObject 接收用户ID ，当前用户ID
     * @return
     */
    @Override
    public JSONObject deleteChatMessage(JSONObject jsonObject) {
        chatDao.deleteChatMessage(jsonObject);
        return CommonUtil.successJson();
    }
}
