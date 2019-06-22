package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/21 17:38
 * @description：板块的Dao层
 * @version: 1.0$
 */
public interface StickDao {

    /**
     * 获取帖子置顶列表
     * @param jsonObject
     * @return
     */
    List<JSONObject> listStick(JSONObject jsonObject);

    /**
     * 获取帖子的置顶情况
     * @param jsonObject
     * @return
     */
    List<JSONObject> listStickByPostId(JSONObject jsonObject);

    /**
     * 置顶帖子
     * @param jsonObject
     * @return
     */
    int addStick(JSONObject jsonObject);

    /**
     * 取消置顶
     * @param
     * @return
     */
    int updatePostStick(JSONObject jsonObject);
}
