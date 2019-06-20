package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.CollectionDao;
import com.heeexy.example.service.CollectionService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author L-YX
 * @description
 * @data 2019-06-18 13:48
 */
@Service
public class CollectionServiceImpl implements CollectionService {
    @Autowired
    private CollectionDao collectionDao;

    @Override
    public JSONObject getByUserId(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = collectionDao.countByUserId(jsonObject);
        List<JSONObject> list = collectionDao.getByUserId(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject countByUserId(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject addCollection(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject deleteCollection(JSONObject jsonObject) {
        return null;
    }
}
