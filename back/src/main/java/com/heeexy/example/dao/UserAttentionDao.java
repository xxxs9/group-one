package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: liminhao
 * @date: 2019-06-27 15:15
 * @description:
 * @version:
 */
public interface UserAttentionDao {

    /**
     * 获取用户的粉丝用户uuID
     * @param uuId
     * @return
     */
    List<JSONObject> getFansUUIDByUUID(JSONObject jsonObject);

    /**
     * 获取用户关注的用户uuid
     * @param uuId
     * @return
     */
    List<JSONObject> getMyIdolIdByUUID(JSONObject jsonObject);

    /**
     * 统计粉丝数量
     * @param uuId
     * @return
     */
    int countFansByUUID(JSONObject jsonObject);

    /**
     * 统计关注数量
     * @param uuId
     * @return
     */
    int countIdolByUUID(JSONObject jsonObject);

    /**
     * 新增关注
     * @param jsonObject
     * @return
     */
    int addAttention(JSONObject jsonObject);

    /**
     * 取消关注
     * @param jsonObject
     * @return
     */
    int removeAttention(JSONObject jsonObject);

    /**
     * 判断是否之前是否关注过并已取关
     * @param jsonObject
     * @return
     */
    int queryExist(JSONObject jsonObject);

    /**
     * 恢复关注
     * @param jsonObject
     * @return
     */
    int refreshAttention(JSONObject jsonObject);

    /**
     * 判断是否已经关注
     * @param jsonObject
     * @return
     */
    int queryAlreadyAttention(JSONObject jsonObject);


}
