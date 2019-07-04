package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ExternalUserDao;
import com.heeexy.example.dao.ThumbsUpDao;
import com.heeexy.example.service.ThumbsUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: linchen
 * @description:
 * @date: 2019/7/1
 * @Version:
 */
@Service
public class ThumbsUpServiceImpl implements ThumbsUpService {

    @Autowired
    private ThumbsUpDao thumbsUpDao;
    @Autowired
    private ExternalUserDao externalUserDao;

    @Override

    public List<JSONObject> getThumbsUp(JSONObject jsonObject) {
        List<JSONObject> likePostList = thumbsUpDao.getLikePostList(jsonObject);
        List<JSONObject> list = new ArrayList<>();

        for (JSONObject postIds : likePostList) {
            List<JSONObject> likeList = thumbsUpDao.getLikeList(postIds);
            for (JSONObject userIDs : likeList) {
                Object userID = userIDs.get("userId");
                Object createTime = userIDs.get("createTime");
                JSONObject jsonObject1 = new JSONObject();
                jsonObject.put("uuId",userID);
                JSONObject iconById = externalUserDao.findIconById(jsonObject);
                Object iconUrl = iconById.get("iconUrl");
                Object username = iconById.get("username");
                jsonObject.put("iconUrl",iconById);
                jsonObject.put("username",username);
                jsonObject.put("createTime",createTime);
                list.add(jsonObject);
            }
        }

        return list;
    }

    @Override
    public List<JSONObject> getMyThumbsUp(JSONObject jsonObject) {
        return null;
    }
}
