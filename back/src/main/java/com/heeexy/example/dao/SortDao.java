package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface SortDao {
    /**
     * 新增文章
     */
    int addArticle(JSONObject jsonObject);

    /**
     * 统计文章总数
     */
    int countSort(JSONObject jsonObject);

    /**
     * 文章列表
     */
    List<JSONObject> listSort(JSONObject jsonObject);
    List<JSONObject> listSortName(JSONObject jsonObject);

    /**
     * 更新文章
     */
    int updateArticle(JSONObject jsonObject);
}
