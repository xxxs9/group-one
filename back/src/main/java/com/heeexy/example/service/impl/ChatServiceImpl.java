package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ChatDao;
import com.heeexy.example.service.ChatService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: linchen
 * @description:
 * @date: 2019/7/2
 * @Version:
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatDao chatDao;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addChatMessage(JSONObject jsonObject) {
        String firstId = (String)jsonObject.get("firstId");
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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject getChatList(JSONObject jsonObject) {
        List<JSONObject> userById = chatDao.getUserById(jsonObject);
        return CommonUtil.successJson(userById);
    }

    @Override
    public JSONObject getChatMessage(JSONObject jsonObject) {
        chatDao.updateChatMessageStatus(jsonObject);
        List<JSONObject> chatMessage = chatDao.getChatMessage(jsonObject);
        return CommonUtil.successJson(chatMessage);
    }

    @Override
    public JSONObject deleteChatMessage(JSONObject jsonObject) {
        chatDao.deleteChatMessage(jsonObject);
        return CommonUtil.successJson();
    }
}
