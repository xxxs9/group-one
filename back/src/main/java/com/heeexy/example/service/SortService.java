package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: linchen
 * @description:
 * @date: 2019/6/18
 * @Version:
 */
public interface SortService {
    /**
     * 新增文章
     */
    JSONObject addSort(JSONObject jsonObject);

    /**
     * 文章列表
     */
    JSONObject listSort(JSONObject jsonObject);
    JSONObject getSortType(JSONObject jsonObject);

    JSONObject listSortName(JSONObject jsonObject);

    /**
     * 更新文章
     */
    JSONObject updateSort(JSONObject jsonObject);
    JSONObject deleteSort(JSONObject jsonObject);
    JSONObject recoverySort(JSONObject jsonObject);

}
