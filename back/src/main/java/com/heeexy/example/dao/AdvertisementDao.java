package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: L-YX
 * @description: 广告方法接口
 * @data: 2019-06-17 20:51
 * @version: 1.0
 */
public interface AdvertisementDao {
    /**
     * 查询广告列表(当广告id和广告名为空时查询全部广告列表，反之根据广告id查询广告列表或广告名模糊查询)
     * @return List<JSONObject>
     */
    List<JSONObject> listAllAdvertisement(JSONObject jsonObject);

    /**
     * 统计广告总数（当广告名为空时查询广告总数，反之根据广告名模糊查询广告总数）
     * @param jsonObject
     * @return int
     */
    int countAdvertisement(JSONObject jsonObject);

    /**
     * 添加广告
     * @param jsonObject
     * @return int
     */
    int addAdvertisement(JSONObject jsonObject);

    /**
     * 修改广告（修改客户需求的不同图片路径）
     * @param jsonObject
     * @return int
     */
    int updateAdvertisement(JSONObject jsonObject);

    /**
     * 修改广告状态，决定播放与否
     * @param jsonObject
     * @return int
     */
    int removeAdvertisement (JSONObject jsonObject);

    /**
     * 根据广告状态查询显示广告
     * @return List<JSONObject>
     */
    List<JSONObject> advertisementList();
}
