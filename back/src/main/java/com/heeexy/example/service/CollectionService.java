package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: L-YX
 * @description:
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
    JSONObject removeCollection(JSONObject jsonObject);

//    /**
//     * 根据帖子id去判断帖子是否被收藏
//     * @param jsonObject
//     * @return jsonObject
//     */
//    JSONObject getByPostId(JSONObject jsonObject);
//
//    /**
//     * 根据用户id去查询此用户收藏的帖子总数
//     * @return jsonObject
//     */
//    JSONObject getByCollectionCount(JSONObject jsonObject);
}
