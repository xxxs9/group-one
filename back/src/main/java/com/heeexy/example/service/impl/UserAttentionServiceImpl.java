package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ExternalUserDao;
import com.heeexy.example.dao.UserAttentionDao;
import com.heeexy.example.service.UserAttentionService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: liminhao
 * @date: 2019-06-28 16:01
 * @description:
 * @version:
 */
@Service
public class UserAttentionServiceImpl implements UserAttentionService {
    
    @Autowired
    UserAttentionDao userAttentionDao;
    @Autowired
    ExternalUserDao externalUserDao;
    @Override
    public JSONObject getFans(JSONObject jsonObject) {

        List<JSONObject> fansUUID = userAttentionDao.getFansUUIDByUUID(jsonObject);
        List<JSONObject> fans = new ArrayList<>();
        for(JSONObject object : fansUUID){
            object.put("uuId",object.getInteger("fansId"));
            fans.add(externalUserDao.findIconById(object));
        }
        
        return CommonUtil.successPage(fans);
    }

    @Override
    public JSONObject getIdol(JSONObject jsonObject) {

        List<JSONObject> myIdolUUID = userAttentionDao.getMyIdolIdByUUID(jsonObject);
        List<JSONObject> idols = new ArrayList<>();
        for(JSONObject object : myIdolUUID){
            object.put("uuId",object.getInteger("idolId"));
            idols.add(externalUserDao.findIconById(object));
        }

        return CommonUtil.successPage(idols);
    }
}

