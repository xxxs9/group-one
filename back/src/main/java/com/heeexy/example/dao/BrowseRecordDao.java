package com.heeexy.example.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: liminhao
 * @date: 2019-06-21 15:43
 * @description: 浏览记录dao层
 * @version:
 */
public interface BrowseRecordDao {
    /**
     * 查询记录列表
     * @return
     */
    List<JSONObject> getRecords(JSONObject jsonObject);

    /**
     * 根据用户名查询用户记录列表
     * @param jsonObject
     * @return
     */
    public List<JSONObject> getRecordsByName(JSONObject jsonObject);

    /**
     * 删除记录
     * @param jsonObject
     * @return
     */
    public int removeRecord(JSONObject jsonObject);

    /**
     * 统计数量
     * @param jsonObject
     * @return
     */
    int countRecord(JSONObject jsonObject);
}
