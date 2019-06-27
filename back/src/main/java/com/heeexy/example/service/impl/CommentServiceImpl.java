package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.CommentDao;
import com.heeexy.example.dao.ExternalUserDao;
import com.heeexy.example.service.CommentService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private ExternalUserDao externalUserDao;

    @Autowired
    private PostServiceImpl postService;

    @Override
    public JSONObject listAllComment(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        if (jsonObject.getString("commentUserName") != null) {
            jsonObject.put("querykey", jsonObject.getString("commentUserName"));
            List<JSONObject> user = externalUserDao.getUser(jsonObject);
            List<Integer> uuidList = new ArrayList<>();
            for (JSONObject object : user) {
                uuidList.add(object.getInteger("uuId"));
            }
            System.out.println(uuidList);
            jsonObject.put("uuidList", uuidList);
        }
        int count = commentDao.countComment(jsonObject);
        List<JSONObject> list = commentDao.listAllComment(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 移除评论
     */
    @Override
    public JSONObject removeComment(JSONObject jsonObject) {
        commentDao.removeComment(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 添加评论
     */
    @Override
    public JSONObject addComment(JSONObject jsonObject) {
        commentDao.addComment(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 我评论过谁
     */
    @Override
    public JSONObject getByCommentUserId(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        jsonObject.put("commentUserId",10001 );
        List<JSONObject> list = commentDao.getByCommentUserId(jsonObject);
        int count = commentDao.countByCommentUserId(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 谁评论过我
     */
    @Override
    public JSONObject getByAcceptUserId(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = commentDao.countByAcceptUserId(jsonObject);
        List<JSONObject> list = commentDao.getByAcceptUserId(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 某帖子下的评论详情
     */
    @Override
    public JSONObject getByPostId(JSONObject jsonObject) {
        int postId = commentDao.getByCommentId(jsonObject);
        jsonObject.put("postId", postId);
        List<JSONObject> list = commentDao.getByPostId(jsonObject);
        int count = commentDao.getByPostIdCount(jsonObject);
        jsonObject.put("postDetail", postService.queryPostById(jsonObject));
        return CommonUtil.successPage(jsonObject, list, count);
    }
}
