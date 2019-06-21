package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: linchen
 * @description:
 * @date: 2019/6/17
 * @Version:
 */
public interface TemplateService {
    /**
     * 新增文章
     */
    JSONObject addTemplate(JSONObject jsonObject);



    /**
     * 文章列表
     */
    JSONObject listTemplate(JSONObject jsonObject);

    /**
     * 更新文章
     */
    JSONObject updateTemplate(JSONObject jsonObject);

    JSONObject updateByStatus(JSONObject jsonObject);

}
