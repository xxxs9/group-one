package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.PostDao;
import com.heeexy.example.service.PostService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/18 10:47
 * @description：
 * @version: $
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDao postDao;

    @Override
    public JSONObject listPost(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = postDao.countUser(jsonObject);
        List<JSONObject> list = postDao.listPost(jsonObject);
        for (JSONObject object : list) {
            object.put("likeCount",Integer.parseInt(object.getString("likeOffset")) + Integer.parseInt(object.getString("likeCount")));
            object.put("browseCount",Integer.parseInt(object.getString("browseOffset")) + Integer.parseInt(object.getString("browseCount")));
        }
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject updateLikeOffset(JSONObject jsonObject) {
        postDao.updateLikeOffset(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject updateBrowseOffset(JSONObject jsonObject) {
        postDao.updateBrowseOffset(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject updatePostState(JSONObject jsonObject) {
        postDao.updatePostState(jsonObject);
        return CommonUtil.successJson();
    }
}
