package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface TagDao {
    /**
     * 新增文章
     */
    JSONObject addTag(JSONObject jsonObject);


    int countTag(JSONObject jsonObject);
    /**
     * 文章列表
     */
    List<JSONObject> listTag(JSONObject jsonObject);

    /**
     * 更新文章
     */
    JSONObject updateTag(JSONObject jsonObject);

    JSONObject updateByStatus(JSONObject jsonObject);
}
