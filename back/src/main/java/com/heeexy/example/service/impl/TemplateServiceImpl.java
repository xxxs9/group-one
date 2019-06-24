package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.TemplateDao;
import com.heeexy.example.service.TemplateService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: linchen
 * @description:
 * @date: 2019/6/17
 * @Version:
 */
@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private TemplateDao templateDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addTemplate(JSONObject jsonObject) {
        templateDao.addTemplate(jsonObject);

        return CommonUtil.successJson();

    }

    @Override
    public JSONObject addChatTemplate(JSONObject jsonObject) {
        List<JSONObject> ulist = templateDao.getAllUserId(jsonObject);
        jsonObject.put("ulist",ulist);
        int i = templateDao.addChatTemplate(jsonObject);

        return CommonUtil.successJson();
    }

    @Override
    public JSONObject listTemplate(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = templateDao.countTemplate(jsonObject);
        List<JSONObject> list = templateDao.listTemplate(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateTemplate(JSONObject jsonObject) {
        templateDao.updateTemplate(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject updateByStatus(JSONObject jsonObject) {
        templateDao.updateByStatus(jsonObject);

        return CommonUtil.successJson();
    }
}
