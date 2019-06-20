package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.AdvertisementDao;
import com.heeexy.example.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author L-YX
 * @description
 * @data 2019-06-18 14:32
 */
@Service
public class AdvertisementServiceImpl implements AdvertisementService {
    @Autowired
    private AdvertisementDao advertisementDao;

    @Override
    public JSONObject listAllAdvertisement(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject countAdvertisement(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject addAdvertisement(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject updateAdvertisement(JSONObject jsonObject) {
        return null;
    }
}
