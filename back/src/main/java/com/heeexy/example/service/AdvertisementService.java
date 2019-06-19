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
     * 查询广告列表(当广告id和广告名为空时查询全部广告列表，反之根据广告id查询广告列表或广告名模糊查询)
     * @return JSONObject
     */
    JSONObject listAllAdvertisement(JSONObject jsonObject);

    /**
     * 统计广告总数（当广告名为空时查询广告总数，反之根据广告名模糊查询广告总数）
     * @param jsonObject
     * @return JSONObject
     */
    JSONObject countAdvertisement(JSONObject jsonObject);

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
}
