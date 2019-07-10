package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ExternalUserDao;
import com.heeexy.example.dao.ThumbsUpDao;
import com.heeexy.example.service.ThumbsUpService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private PostServiceImpl postService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject getThumbsUp(JSONObject jsonObject) {
        List<JSONObject> likePostList = thumbsUpDao.getLikePostList(jsonObject);
        List<JSONObject> list = new ArrayList<>();

        for (JSONObject postIds : likePostList) {
            List<JSONObject> likeList = thumbsUpDao.getLikeList(postIds);
            for (JSONObject userIDs : likeList) {
                Object userID = userIDs.get("userId");
                Date createTime = userIDs.getDate("createTime");
                String s = StringTools.differentDaysByMillisecond(createTime);
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("uuId",userID);
                JSONObject iconById = externalUserDao.findIconById(jsonObject1);
                Object iconUrl = iconById.get("iconUrl");
                Object username = iconById.get("username");
                jsonObject1.put("likeimg",iconUrl);
                jsonObject1.put("likename",username);
                jsonObject1.put("time",s);
                list.add(jsonObject1);
            }
        }

        return CommonUtil.successJson(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject getMyThumbsUp(JSONObject jsonObject) {
        List<JSONObject> postIdByUserId = thumbsUpDao.getMyLikePostList(jsonObject);
        jsonObject.put("postIdList", postIdByUserId);
        List<JSONObject> postListApi = postService.getPostListApi(jsonObject);

        return CommonUtil.successJson(postListApi);
    }

    @Override
    public JSONObject updateThumbsUp(JSONObject jsonObject) {
        int i = thumbsUpDao.queryExist(jsonObject);
        if(i>0) {
            thumbsUpDao.removeLike(jsonObject);
        }else {
            thumbsUpDao.addLike(jsonObject);
        }
        return CommonUtil.successJson();
    }


}
