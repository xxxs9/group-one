package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.CommentDao;
import com.heeexy.example.service.CommentService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author L-YX
 * @description
 * @data 2019-06-18 11:25
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;

    @Override
    public JSONObject listAllComment(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = commentDao.countComment(jsonObject);
        List<JSONObject> list = commentDao.listAllComment(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject countComment(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject updateComment(JSONObject jsonObject) {
        commentDao.updateComment(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addComment(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject getByCommentUserId(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject countByCommentUserId(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject getByAcceptUserId(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject countByAcceptUserId(JSONObject jsonObject) {
        return null;
    }
}
