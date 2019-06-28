package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.AdvertisementDao;
import com.heeexy.example.service.AdvertisementService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author L-YX
 * @description 广告逻辑处理实现方法
 * @data 2019-06-18 14:32
 */
@Service
public class AdvertisementServiceImpl implements AdvertisementService {
    @Autowired
    private AdvertisementDao advertisementDao;

    /**
     * 后台广告列表
     */
    @Override
    public JSONObject listAllAdvertisement(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        List<JSONObject> list = advertisementDao.listAllAdvertisement(jsonObject);
        int count = advertisementDao.countAdvertisement(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 增加广告
     */
    @Override
    public JSONObject addAdvertisement(JSONObject jsonObject) {
        JSONArray src = jsonObject.getJSONArray("imgList");
        for (Object o : src) {
            jsonObject.put("advertisementStatus",1 );
            jsonObject.put("srcUrl",o );
            advertisementDao.addAdvertisement(jsonObject);
        }
        return CommonUtil.successJson();
    }

    /**
     * 修改广告
     */
    @Override
    public JSONObject updateAdvertisement(JSONObject jsonObject) {
        JSONArray src = jsonObject.getJSONArray("imgList");
        String url = jsonObject.getString("srcUrl");
        String desFilePath = url.replaceFirst("http://localhost:8080/","D://" );
        File localFile  = new File(desFilePath);
        localFile.delete();
        if (src.size()!=0){
            for (Object o : src) {
                jsonObject.put("srcUrl",o );
            }
        }
        advertisementDao.updateAdvertisement(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 删除广告
     */
    @Override
    public JSONObject removeAdvertisement(JSONObject jsonObject) {
        advertisementDao.removeAdvertisement(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 广告轮播
     */
    @Override
    public JSONObject advertisementList(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        List<JSONObject> list = advertisementDao.advertisementList();
        int count = 6;
        return CommonUtil.successPage(jsonObject, list, count);
    }
}
