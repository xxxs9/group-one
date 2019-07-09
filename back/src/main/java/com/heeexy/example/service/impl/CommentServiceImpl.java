package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.CommentDao;
import com.heeexy.example.dao.ExternalUserDao;
import com.heeexy.example.dao.PostDao;
import com.heeexy.example.service.CommentService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author L-YX
 * @description 评论逻辑处理实现方法
 * @data 2019-06-18 11:25
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ExternalUserDao externalUserDao;

    @Autowired
    private PostDao postDao;

    /**
     * 后台评论列表
     * @param jsonObject (key:commentText,commentTime,commentUserName)
     * @return JSONObject
     */
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
     * @param jsonObject (key:commentId)
     * @return JSONObject
     */
    @Override
    public JSONObject removeComment(JSONObject jsonObject) {
        commentDao.removeComment(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 前台添加评论
     * @param jsonObject (key:postId,postUserId,commentUserId,acceptUserId,commentText)
     * @return JSONObject
     */
    @Override
    public JSONObject addComment(JSONObject jsonObject) {
        jsonObject.put("commentState", 1);
        commentDao.addComment(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 前台我评论过谁
     * @param jsonObject (key:commentUserId)
     * @return JSONObject
     */
    @Override
    public JSONObject getByCommentUserId(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        List<JSONObject> list = commentDao.getByCommentUserId(jsonObject);
        int count = commentDao.countByCommentUserId(jsonObject);
        JSONObject commentUser = new JSONObject();
        commentUser.put("commentUserList",list );
        commentUser.put("count", count);
        return CommonUtil.successJson(commentUser);
    }

    /**
     * 前台谁评论过我
     * @param jsonObject (key:acceptUserId)
     * @return JSONObject
     */
    @Override
    public JSONObject getByAcceptUserId(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = commentDao.countByAcceptUserId(jsonObject);
        List<JSONObject> list = commentDao.getByAcceptUserId(jsonObject);
        for (JSONObject object : list) {
            object.put("commentsid", object.remove("acceptuserid"));
            object.put("commentsname", object.remove("acceptusername"));
            object.put("commentsimg", object.remove("acceptsimg"));
            object.put("commentsdesc", object.remove("commenttext"));
            object.put("commentstime", object.remove("commenttime"));
        }
        JSONObject acceptUser = new JSONObject();
        acceptUser.put("acceptUserList",list );
        acceptUser.put("count", count);
//        acceptUser.put("", acceptUser.remove("acceptUserList"));
        return CommonUtil.successJson(acceptUser);
    }

    /**
     * 某帖子下的评论详情
     * @param jsonObject (key:postId)
     * @return JSONObject
     */
    @Override
    public JSONObject getByPostId(JSONObject jsonObject) {
        int postId = commentDao.getByCommentId(jsonObject);
        jsonObject.put("postId", postId);
        JSONObject DetailData = postDao.queryPostById(jsonObject);
        List<JSONObject> comment = commentDao.getByPostId(jsonObject);
        JSONObject postUser = externalUserDao.findUserById(DetailData.getInteger("postOwnerId"));
        DetailData.put("externalUser",postUser);
        DetailData.put("comments",comment);
        return CommonUtil.successJson(DetailData);
    }

}
