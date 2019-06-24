package com.heeexy.example.service;


import com.alibaba.fastjson.JSONObject;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/21 21:50
 * @description：板块的Service层
 * @version: 0.1$
 */
public interface StickService {

    /**
     * 获取板块列表
     * @param jsonObject 无需参数
     * @return 板块列表
     */
    JSONObject listStick(JSONObject jsonObject);

    /**
     * 获取帖子置顶状态
     * @param jsonObject 帖子ID
     * @return
     */
    JSONObject listStickByPostId(JSONObject jsonObject);

    /**
     * 修改帖子状态
     * @param jsonObject 帖子ID ，置顶板块ID
     * @return
     */
    JSONObject updatePostStick(JSONObject jsonObject);

}
