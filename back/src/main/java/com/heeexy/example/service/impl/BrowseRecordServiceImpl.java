package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.BrowseRecordDao;
import com.heeexy.example.service.BrowseRecordService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: liminhao
 * @date: 2019-06-21 16:52
 * @description: service层
 * @version:
 */
@Service
public class BrowseRecordServiceImpl implements BrowseRecordService {
    @Autowired
    BrowseRecordDao browseRecordDao;
    @Override
    public JSONObject getRecords(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = browseRecordDao.countRecord(jsonObject);
        List<JSONObject> list = browseRecordDao.getRecords(jsonObject);
//        System.out.println(jsonObject.getString("querykey"));
        return CommonUtil.successPage(jsonObject, list, count);

    }

    @Override
    public List<JSONObject> getRecordsByName(JSONObject jsonObject) {

        return null;
    }
}

