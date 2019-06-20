package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: L-YX
 * @description:
 * @data: 2019-06-17 20:51
 * @version: 1.0
 */
public interface CollectionDao {
    /**
     * 根据用户id去查询该用户下收藏的帖子列表
     * @param jsonObject
     * @return List<JSONObject>
     */
    List<JSONObject> getByUserId(JSONObject jsonObject);

    /**
     * 根据用户id去查询该用户下收藏的帖子总数
     * @param jsonObject
     * @return int
     */
    int countByUserId(JSONObject jsonObject);

    /**
     * 添加收藏
     * @param jsonObject
     * @return int
     */
    int addCollection(JSONObject jsonObject);

    /**
     * 根据用户需求来通过收藏id移除收藏里的帖子
     * @param jsonObject
     * @return int
     */
    int deleteCollection(JSONObject jsonObject);
}
