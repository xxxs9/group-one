package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface TemplateDao {
    /**
     * 新增文章
     */
    int addTemplate(JSONObject jsonObject);

    /**
     * 统计文章总数
     */
    int countTemplate(JSONObject jsonObject);

    /**
     * 文章列表
     */
    List<JSONObject> listTemplate(JSONObject jsonObject);

    /**
     * 更新文章
     */
    int updateTemplate(JSONObject jsonObject);
}
