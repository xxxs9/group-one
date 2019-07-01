package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: L-YX
 * @description: 广告逻辑处理方法接口
 * @data: 2019-06-18 14:31
 * @version: 1.0
 */
public interface AdvertisementService {
    /**
     * 查询广告列表(当广告标题为空时无条件查询全部广告列表，反之根据广告标题模糊查询)
     * @param jsonObject (key:advertisementType)
     * @return List<JSONObject>
     */
    JSONObject listAllAdvertisement(JSONObject jsonObject);

    /**
     * 添加广告
     * @param jsonObject (key:advertisementRef,srcUrl,advertisementType,advertisementStatus)
     * @return JSONObject
     */
    JSONObject addAdvertisement(JSONObject jsonObject);

    /**
     * 修改广告（修改客户需求图片链接，图片路径和图片标题）
     * @param jsonObject (key:advertisementRef,srcUrl,advertisementType,advertisementId)
     * @return JSONObject
     */
    JSONObject updateAdvertisement(JSONObject jsonObject);

    /**
     * 修改广告状态，决定播放与否
     * @param jsonObject (key:advertisementStatus,advertisementId)
     * @return JSONObject
     */
    JSONObject removeAdvertisement (JSONObject jsonObject);

    /**
     * 根据广告状态查询显示广告
     * @param jsonObject (无有用参数)
     * @return JSONObject
     */
    JSONObject advertisementList(JSONObject jsonObject);
}
