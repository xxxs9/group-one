package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: L-YX
 * @description:
 * @data: 2019-06-18 14:31
 * @version: 1.0
 */
public interface AdvertisementService {
    /**
     * 查询广告列表(当广告类型为空时查询全部广告列表，反之根据广告类型模糊查询)
     * @return JSONObject
     */
    JSONObject listAllAdvertisement(JSONObject jsonObject);

    /**
     * 添加广告
     * @param jsonObject
     * @return JSONObject
     */
    JSONObject addAdvertisement(JSONObject jsonObject);

    /**
     * 修改广告（一般是修改同一客户需求的不同图片路径）
     * @param jsonObject
     * @return JSONObject
     */
    JSONObject updateAdvertisement(JSONObject jsonObject);

    /**
     * 修改广告状态，决定播放与否
     * @param jsonObject
     * @return int
     */
    JSONObject removeAdvertisement (JSONObject jsonObject);

    /**
     * 根据广告状态查询显示广告
     * @param jsonObject
     * @return JSONObject
     */
    JSONObject advertisementList(JSONObject jsonObject);
}
