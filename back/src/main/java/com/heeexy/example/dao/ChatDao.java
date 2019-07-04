package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ChatDao {

    int addFirstChatMessage (JSONObject jsonObject);
    int addSecondChatMessage (JSONObject jsonObject);
    int updateChatMessageStatus (JSONObject jsonObject);
    int deleteChatMessage (JSONObject jsonObject);
    List<JSONObject> getUserById (JSONObject jsonObject);
    List<JSONObject> getChatMessage (JSONObject jsonObject);


}
