package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ExternalUserDao;
import com.heeexy.example.dao.UserAttentionDao;
import com.heeexy.example.service.UserAttentionService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
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

    /**
     * 获取粉丝列表
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject getFans(JSONObject jsonObject) {

        List<JSONObject> fansUUID = userAttentionDao.getFansUUIDByUUID(jsonObject);
        List<JSONObject> fans = new ArrayList<>();
        for(JSONObject object : fansUUID){
            object.put("uuId",object.getInteger("fansId"));
            JSONObject fan = externalUserDao.findIconById(object);
            fan.put("fansid",object.getInteger("fansId"));
            fan.put("fansname",fan.getString("username"));
            fan.put("fansavatar",fan.getString("iconUrl"));
            fan.remove("username");
            fan.remove("iconUrl");
            fans.add(fan);
        }
        
        return CommonUtil.successPage(fans);
    }

    /**
     * 获取关注列表
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject getIdol(JSONObject jsonObject) {

        List<JSONObject> myIdolUUID = userAttentionDao.getMyIdolIdByUUID(jsonObject);
        List<JSONObject> idols = new ArrayList<>();
        for(JSONObject object : myIdolUUID){
            object.put("uuId",object.getInteger("idolId"));
            JSONObject idol = externalUserDao.findIconById(object);
            idol.put("attentionid",object.getInteger("idolId"));
            idol.put("attentionname",idol.getString("username"));
            idol.put("attentionavatar",idol.getString("iconUrl"));
            idol.remove("username");
            idol.remove("iconUrl");
            idols.add(idol);
        }

        return CommonUtil.successPage(idols);
    }

    /**
     * 新增关注
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject addAttention(JSONObject jsonObject) {

        Integer otherId = jsonObject.getInteger("otherId");
        jsonObject.put("touuId",otherId);
        int i = userAttentionDao.queryAlreadyAttention(jsonObject);
        if(userAttentionDao.queryExist(jsonObject)>0){
            userAttentionDao.refreshAttention(jsonObject);
        }
        else if(userAttentionDao.queryAlreadyAttention(jsonObject)==0) {
            userAttentionDao.addAttention(jsonObject);
        }
        else if(userAttentionDao.queryAlreadyAttention(jsonObject)>0){
            userAttentionDao.removeAttention(jsonObject);
        }


        return CommonUtil.successJson();
    }

    /**
     * 取消关注
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject removeAttention(JSONObject jsonObject) {

        Integer otherId = jsonObject.getInteger("otherId");
        jsonObject.put("touuId",otherId);

        return CommonUtil.successJson();
    }


}

