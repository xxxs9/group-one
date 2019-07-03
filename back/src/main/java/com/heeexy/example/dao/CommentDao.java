package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

/**
 * @author: L-YX
 * @description: 评论方法接口
 * @data: 2019-06-17 20:50
 * @version: 1.0
 */
public interface CommentDao {

    /**
     *当评论内容为空时查询全部评论列表，反之根据评论内容模糊查询评论列表
     * @param jsonObject (key:commentText,commentTime,commentUserName)
     * @return List<JSONObject>
     */
    List<JSONObject> listAllComment(JSONObject jsonObject);

    /**
     * 当没有条件的时候查询全部评论的总数，反之根据条件查询评论的总数
     * @param jsonObject (key:commentText,commentTime,commentUserName)
     * @return int
     */
    int countComment(JSONObject jsonObject);

    /**
     * 根据评论id去改变评论的状态来决定评论的显示/隐藏
     * @param jsonObject (key:commentId)
     * @return int
     */
    int removeComment(JSONObject jsonObject);

    /**
     * 添加评论
     * @param jsonObject (key:postId,postUserId,commentUserId,acceptUserId,commentText)
     * @return int
     */
    int addComment(JSONObject jsonObject);

    /**
     * 根据评论用户id查询评论
     * @param jsonObject (key:commentUserId)
     * @return int
     */
    List<JSONObject> getByCommentUserId(JSONObject jsonObject);

    /**
     * 根据评论用户id查询评论总数
     * @param jsonObject (key:commentUserId)
     * @return int
     */
    int countByCommentUserId(JSONObject jsonObject);

    /**
     * 根据接收评论用户id查询评论(acceptUserId)
     * @param jsonObject (key:acceptUserId)
     * @return List<JSONObject>
     */
    List<JSONObject> getByAcceptUserId(JSONObject jsonObject);

    /**
     * 根据接收评论用户id查询评论总数
     * @param jsonObject (key:acceptUserId)
     * @return int
     */
    int countByAcceptUserId(JSONObject jsonObject);

    /**
     * 根据帖子id查询评论
     * @param jsonObject (key:postId)
     * @return List<JSONObject>
     */
    List<JSONObject> getByPostId(JSONObject jsonObject);

    /**
     * 根据评论id查帖子id
     * @param jsonObject (key:commentId)
     * @return int
     */
    int getByCommentId(JSONObject jsonObject);

    /**
     * 根据帖子id查询评论总数
     * @param jsonObject (key:postId)
     * @return int
     */
    int getByPostIdCount(JSONObject jsonObject);
}
