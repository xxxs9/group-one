package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.TagDao;
import com.heeexy.example.service.TagSevice;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: linchen
 * @description:
 * @date: 2019/6/21
 * @Version:
 */
@Service
public class TagSeviceImpl implements TagSevice {
    @Autowired
    private TagDao tagDao;

    @Override
    public JSONObject addTag(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject listTag(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = tagDao.countTag(jsonObject);
        List<JSONObject> list = tagDao.listTag(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject listAllTag(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = tagDao.countTag(jsonObject);
        List<JSONObject> list = tagDao.listAllTag(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject updateTag(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject deleteTag(JSONObject jsonObject) {
/*
        System.out.println(jsonObject);
*/
        tagDao.updateByStatus(jsonObject);
        return CommonUtil.successJson();
    }
}
