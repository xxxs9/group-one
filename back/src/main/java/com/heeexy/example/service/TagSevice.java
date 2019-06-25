package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface TagSevice {

    JSONObject addTag(JSONObject jsonObject);

    /**
     * 文章列表
     */
    JSONObject listTag(JSONObject jsonObject);

    JSONObject listAllTag(JSONObject jsonObject);


    /**
     * 更新文章
     */
    JSONObject updateTag(JSONObject jsonObject);
    JSONObject deleteTag(JSONObject jsonObject);
}
