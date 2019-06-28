package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

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
    JSONObject addUser(JSONObject jsonObject);

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
     * 将用户曾经拥有而修改为不再拥有的权限 delete_status改为'2'
     * @param uuId
     * @param permissions
     * @return
     */
    JSONObject updatePermission(JSONObject jsonObject);

    /**
     * 修改用户被禁用的权限状态 delete_status改为'1'
     * @param uuId
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
     * 根据UUID获取该用户所发的帖子id
     * @param jsonObject
     * @return
     */
    JSONObject getPostByUUID(JSONObject jsonObject);

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
}

