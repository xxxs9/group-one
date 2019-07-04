package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

/**
 * @author: liminhao
 * @date: 2019-06-18 11:29
 * @description: 用户/权限
 * @version: 1.0
 */
public interface ExternalUserService {
    /**
     * 用户列表
     */
    JSONObject getUser(JSONObject jsonObject);

    /**
     * 添加用户
     */
    JSONObject userLogin(JSONObject jsonObject);

    /**
     * 修改用户
     */
    JSONObject updateFansOffset(JSONObject jsonObject);

    /**
     * 获取用户权限
     * @param jsonObject
     * @return
     */
    JSONObject getUserPermission(JSONObject jsonObject);

    /**
     * 修改用户的权限状态
     * @param jsonObject
     * @return
     */
    JSONObject updatePermission(JSONObject jsonObject);

    /**
     * 修改用户被禁用的权限状态 delete_status改为'1'
     * @param jsonObject
     * @return
     */
    JSONObject refreshPermissionStatus(JSONObject jsonObject);

    /**
     * 给用户添加权限
     * @param jsonObject
     * @return
     */
    JSONObject addPermission(JSONObject jsonObject);

    /**
     * 移除用户的所有权限
     * @param jsonObject
     * @return
     */
    JSONObject removePermission(JSONObject jsonObject);

    /**
     * 获取用户的粉丝列表信息
     *@param jsonObject
     * @return
     */
    JSONObject getFans(JSONObject jsonObject);

    /**
     * 查询权限列表
     * @param jsonObject
     * @return
     */
    JSONObject getPerm(JSONObject jsonObject);


    /**
     * 获取用户点赞过的帖子id
     * @param jsonObject
     * @return
     */
    JSONObject getPostIdByLike(JSONObject jsonObject);

    /**
     * 根据uuId查询用户头像
     * @param jsonObject
     * @return
     */
    JSONObject findIconById(JSONObject jsonObject);

    /**
     * 获取用户信息
     * @param jsonObject
     * @return
     */
    JSONObject getMyself(JSONObject jsonObject);

    /**
     * 获取其他用户信息
     * @param jsonObject
     * @return
     */
    JSONObject getOthers(JSONObject jsonObject);

    /**
     * 获取该用户发布的帖子
     * @param jsonObject
     * @return
     */
    JSONObject getMyPost(JSONObject jsonObject);

    /**
     * 获取该用户点赞过的帖子
     * @param jsonObject
     * @return
     */
    JSONObject getMyLikePost(JSONObject jsonObject);

    /**
     * 获取该用户的浏览记录
     * @param jsonObject
     * @return
     */
    JSONObject getMyRecords(JSONObject jsonObject);

    /**
     * 获取该用户的评论记录
     * @param jsonObject
     * @return
     */
    JSONObject getMyComments(JSONObject jsonObject);

    /**
     * 判断是否有发帖权限
     * @param jsonObject
     * @return
     */
    Boolean isHasPostPerm(JSONObject jsonObject);

    /**
     * 判断是否有评论权限
     * @param jsonObject
     * @return
     */
    Boolean isHasCommentPerm(JSONObject jsonObject);

    /**
     * 判断是否有私信权限
     * @param jsonObject
     * @return
     */
    Boolean isHasChatPerm(JSONObject jsonObject);

    /**
     * 判断是否有点赞权限
     * @param jsonObject
     * @return
     */
    Boolean isHasLikePerm(JSONObject jsonObject);
}

