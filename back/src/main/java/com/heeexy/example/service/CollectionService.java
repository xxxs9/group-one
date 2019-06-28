package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: L-YX
 * @description: 收藏逻辑处理方法接口
 * @data: 2019-06-18 13:46
 * @version: 1.0
 */
public interface CollectionService {
    /**
     * 根据用户id去查询该用户下收藏的帖子列表
     * @param jsonObject
     * @return JSONObject
     */
    JSONObject getByUserId(JSONObject jsonObject);

    /**
     * 添加收藏
     * @param jsonObject
     * @return JSONObject
     */
    JSONObject addCollection(JSONObject jsonObject);

    /**
     * 根据用户需求来通过收藏id移除收藏里的帖子
     * @param jsonObject
     * @return JSONObject
     */
    JSONObject deleteCollection(JSONObject jsonObject);
}
