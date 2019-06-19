package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: L-YX
 * @description:
 * @data: 2019-06-17 20:50
 * @version: 1.0
 */
public interface CommentDao {

    /**
     *当评论内容和用户名为空时查询全部评论列表，反之根据用户名或者评论内容模糊查询评论列表
     * @param jsonObject
     * @return List<JSONObject>
     */
    List<JSONObject> listAllComment(JSONObject jsonObject);

    /**
     * 当没有条件的时候查询全部评论的总数，反之根据条件查询评论的总数
     * @param jsonObject
     * @return
     */
    int countComment(JSONObject jsonObject);

    /**
     * 根据评论id去改变评论的状态来决定评论的显示/隐藏
     * @param
     * @return int
     */
    int updateComment(JSONObject jsonObject);

    /**
     * 添加评论
     * @param jsonObject
     * @return int
     */
    int addComment(JSONObject jsonObject);

    /**
     * 根据评论用户id查询评论（commentUserId）
     * @param jsonObject
     * @return int
     */
    List<JSONObject> getByCommentUserId(JSONObject jsonObject);

    /**
     * 根据评论用户id查询评论总数
     * @param jsonObject
     * @return
     */
    int countByCommentUserId(JSONObject jsonObject);

    /**
     * 根据接收评论用户id查询评论(acceptUserId)
     * @param jsonObject
     * @return List<JSONObject>
     */
    List<JSONObject> getByAcceptUserId(JSONObject jsonObject);

    /**
     * 根据接收评论用户id查询评论总数
     * @param jsonObject
     * @return
     */
    int countByAcceptUserId(JSONObject jsonObject);
}
