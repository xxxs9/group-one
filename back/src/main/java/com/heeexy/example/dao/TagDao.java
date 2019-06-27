package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface TagDao {
    /**
     * 新增文章
     */
    int addTag(JSONObject jsonObject);

    int addListTag(Tag tag);


    int countTag(JSONObject jsonObject);
    /**
     * 文章列表
     */

    List<JSONObject> listTag(JSONObject jsonObject);

    List<JSONObject> listAllTag(JSONObject jsonObject);

    /**
     * 更新文章
     */
    int updateTag(JSONObject jsonObject);

    int updateByStatus(JSONObject jsonObject);

    /**
     * 根据标签名查找标签ID
     * @param jsonObject 标签名
     * @return 标签ID
     */
    JSONObject getTagByName(JSONObject jsonObject);
}
