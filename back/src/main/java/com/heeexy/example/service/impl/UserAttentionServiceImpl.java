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
            JSONObject fan = externalUserDao.findIconById(object);
            fan.put("likeid",object.getInteger("fansId"));
            fan.put("likename",fan.getString("username"));
            fan.put("likeimg",fan.getString("iconUrl"));
            fan.remove("username");
            fan.remove("iconUrl");
            fans.add(fan);
        }
        
        return CommonUtil.successPage(fans);
    }

    @Override
    public JSONObject getIdol(JSONObject jsonObject) {

        List<JSONObject> myIdolUUID = userAttentionDao.getMyIdolIdByUUID(jsonObject);
        List<JSONObject> idols = new ArrayList<>();
        for(JSONObject object : myIdolUUID){
            object.put("uuId",object.getInteger("idolId"));
            JSONObject idol = externalUserDao.findIconById(object);
            idol.put("likeid",object.getInteger("idolId"));
            idol.put("likename",idol.getString("username"));
            idol.put("likeimg",idol.getString("iconUrl"));
            idol.remove("username");
            idol.remove("iconUrl");
            idols.add(idol);
        }

        return CommonUtil.successPage(idols);
    }
}

