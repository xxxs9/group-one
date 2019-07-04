package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface ChatService {
    JSONObject addChatMessage (JSONObject jsonObject);
    JSONObject getChatList  (JSONObject jsonObject);
    JSONObject getChatMessage  (JSONObject jsonObject);
}
