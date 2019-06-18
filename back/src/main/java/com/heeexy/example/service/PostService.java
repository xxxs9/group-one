package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/18 10:43
 * @description：帖子Service接口
 * @version: 1.0$
 */
public interface PostService {
    /**
     * @description: 帖子列表
     * @create time: 2019/6/18 10:44
     * @param jsonObject 页码
     * @return 返回帖子列表
     */
    JSONObject listPost(JSONObject jsonObject);
}
