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
     * @param jsonObject 页码
     * @return 返回帖子列表
     */
    JSONObject listPost(JSONObject jsonObject);

    /**
     * 增加点赞数量
     * @param jsonObject 帖子ID，点赞数量
     * @return
     */
    JSONObject updateLikeOffset(JSONObject jsonObject);

    /**
     * 增加浏览数量
     * @param jsonObject 帖子ID，浏览量
     * @return
     */
    JSONObject updateBrowseOffset(JSONObject jsonObject);

    /**
     * 改变帖子状态
     * @param jsonObject 帖子ID，状态 ：0为显示 1为隐藏
     * @return
     */
    JSONObject updatePostState(JSONObject jsonObject);
}
