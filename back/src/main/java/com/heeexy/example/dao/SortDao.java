package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface SortDao {
    /**
     * 新增文章
     */
    int addSort(JSONObject jsonObject);

    /**
     * 统计文章总数
     */
    int countSort(JSONObject jsonObject);

    /**
     * 文章列表
     */
    List<JSONObject> listSort(JSONObject jsonObject);
    List<JSONObject> listSortName(JSONObject jsonObject);
    List<JSONObject> getSortType(JSONObject jsonObject);

    /**
     * 更新文章
     */
    int updateSort(JSONObject jsonObject);

    int deleteSort(JSONObject jsonObject);
    int recoverySort(JSONObject jsonObject);
    List<JSONObject> getSortByName(JSONObject jsonObject);
    List<JSONObject> getSortById(JSONObject jsonObject);

    /**
     * 根据类型名查询类型ID
     * @param typeName 类型名
     * @return
     */
    int getIdByName(String typeName);
}
