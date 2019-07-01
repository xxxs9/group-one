package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ThumbsUpDao {
    /**
     * 统计点赞数量
     * @param jsonObject
     * @return
     */
    int countLikes (JSONObject jsonObject);
    List<JSONObject> getLikeList(JSONObject jsonObject);

    /**
     * 新增点赞
     * @param jsonObject
     * @return
     */
    int addLike(JSONObject jsonObject);

    /**
     * 取消点赞
     * @param jsonObject
     * @return
     */
    int removeLike(JSONObject jsonObject);

    /**
     * 点赞记录是否存在
     * @param jsonObject
     * @return
     */
    int queryExist(JSONObject jsonObject);


}
