package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: liminhao
 * @date: 2019-06-28 15:23
 * @description: 用户关注的service层
 * @version:
 */
public interface UserAttentionService {

    /**
     * 获取粉丝列表
     * @param jsonObject
     * @return
     */
    JSONObject getFans(JSONObject jsonObject);

    /**
     * 获取关注用户
     * @param jsonObject
     * @return
     */
    JSONObject getIdol(JSONObject jsonObject);




}
