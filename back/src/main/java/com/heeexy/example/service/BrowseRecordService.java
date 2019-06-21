package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: liminhao
 * @date: 2019-06-21 16:48
 * @description:
 * @version:
 */
public interface BrowseRecordService {

    /**
     * 查询记录列表
     * @return
     */
    public List<JSONObject> getRecords();

    /**
     * 根据用户名查询用户记录列表
     * @param jsonObject
     * @return
     */
    public List<JSONObject> getRecordsByName(JSONObject jsonObject);
}
