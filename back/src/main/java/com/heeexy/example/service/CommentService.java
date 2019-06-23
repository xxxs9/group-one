package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: L-YX
 * @description:
 * @data: 2019-06-18 11:20
 * @version: 1.0
 */
public interface CommentService {

    /**
     *当评论内容和用户名为空时查询全部评论列表，反之根据用户名或者评论内容模糊查询评论列表
     * @param jsonObject
     * @return JSONObject
     */
    JSONObject listAllComment(JSONObject jsonObject);

    /**
     * 根据评论id去改变评论的状态来决定评论的显示/隐藏
     * @param jsonObject
     * @return JSONObject
     */
    JSONObject updateComment(JSONObject jsonObject);

    /**
     * 添加评论
     * @param jsonObject
     * @return JSONObject
     */
    JSONObject addComment(JSONObject jsonObject);

    /**
     * 根据评论用户查询评论（commentUserId）
     * @param jsonObject
     * @return JSONObject
     */
    JSONObject getByCommentUserId(JSONObject jsonObject);

    /**
     * 根据接收评论用户查询评论(acceptUserId)
     * @param jsonObject
     * @return JSONObject
     */
    JSONObject getByAcceptUserId(JSONObject jsonObject);
}
