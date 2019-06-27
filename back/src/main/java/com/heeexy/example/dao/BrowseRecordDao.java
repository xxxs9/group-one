package com.heeexy.example.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

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
    public int updateRecord(JSONObject jsonObject);

    /**
     * 统计数量
     * @param jsonObject
     * @return
     */
    int countRecord(JSONObject jsonObject);

    /**
     * 根据帖子id查询浏览过此帖子的用户列表
     * @param jsonObject
     * @return
     */
    List<JSONObject> getUUIDByPostId(JSONObject jsonObject);

    /**
     * 根据uuid查询用户浏览过的帖子
     * @param jsonObject
     * @return
     */
    List<JSONObject> getRecordsByUUID(JSONObject jsonObject);

    /**
     * 新增记录
     * @param jsonObject
     * @return
     */
    int addRecord(JSONObject jsonObject);
}
