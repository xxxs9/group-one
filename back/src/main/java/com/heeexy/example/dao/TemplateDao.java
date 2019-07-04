package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface TemplateDao {
    /**
     * 新增文章
     */
    int addTemplate(JSONObject jsonObject);
    int addAllUserTemplate(JSONObject jsonObject);
    int addWarningTemplate(JSONObject jsonObject);
    /**
     * 统计文章总数
     */
    int countTemplate(JSONObject jsonObject);
    List<JSONObject> getAllUserId(JSONObject jsonObject);

    /**
     * 文章列表
     */
    List<JSONObject> listTemplate(JSONObject jsonObject);

    /**
     * 更新文章
     */
    int updateTemplate(JSONObject jsonObject);

    int updateByStatus(JSONObject jsonObject);

    List<JSONObject> getTemplateByName(JSONObject jsonObject);
    List<JSONObject> getTemplateById(JSONObject jsonObject);
    String getWarningContentByName();


}
