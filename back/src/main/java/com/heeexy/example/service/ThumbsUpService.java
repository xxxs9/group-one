package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ThumbsUpService {

    /**
     * create by: lc
     * description: 收到的点赞
     * create time: 2019/7/1 15:58
     */
    List<JSONObject> getThumbsUp(JSONObject jsonObject);

    List<JSONObject> getMyThumbsUp(JSONObject jsonObject);

}
