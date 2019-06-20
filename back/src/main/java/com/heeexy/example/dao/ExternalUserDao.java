package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author: liminhao
 * @description: 用户/权限
 * @date: 2019-06-18 09:06
 */
public interface ExternalUserDao {
    /**
     * @author liminhao
     * @date 2019-06-18 09:06
     * @param jsonObject
     * @return  List
     * @description 查询用户列表
    */
    public List<JSONObject> getUser(JSONObject jsonObject);

    /**
     * @author liminhao
     * @date 2019-06-18 09:09
     * @param null
     * @return
     * @description 查询用户数量
    */
    public int countUser(JSONObject jsonObject);

    /**
     * @author liminhao
     * @date 2019-06-18 09:14
     * @param null
     * @return
     * @description 添加用户
    */
    public int addUser(JSONObject jsonObject);

    /**
     * @author liminhao
     * @date 2019-06-18 09:15
     * @param null
     * @return
     * @description 修改用户
    */
    public int updateFansOffset(JSONObject jsonObject);

    /**
     * @author liminhao
     * @date 2019-06-18 09:16
     * @param null
     * @return
     * @description 获取用户所有权限
    */
    public List<JSONObject> getUserPermission(JSONObject jsonObject);

    /**
     * @author liminhao
     * @date 2019-06-18 09:30
     * @param null
     * @return
     * @description 将用户曾经拥有而修改为不再拥有的权限 delete_status改为'2'
    */
    public int removePermission(@Param("uuId") Integer uuId, @Param("epermissions") List<Integer> permissions);

    /**
     * @author liminhao
     * @date 2019-06-18 09:33
     * @param null
     * @return
     * @description 给用户添加权限
    */
    public int addPermission(@Param("uuId") Integer uuId, @Param("epermissions") List<Integer> permissions);

    /**
     * @author liminhao
     * @date 2019-06-18 09:50
     * @param null
     * @return
     * @description 修改用户被禁用的权限状态 delete_status改为'1'
    */
    public int updatePermissionStatus(@Param("uuId") Integer uuId);

    /**
     * @author liminhao
     * @date 2019-06-18 09:51
     * @param null
     * @return
     * @description 移除用户的所有权限
    */
    public int removeUserAllPermission(JSONObject jsonObject);

    /**
     * 校验uuid是否重复
     * @param jsonObject
     * @return
     */
    public int queryExistUUID(JSONObject jsonObject);


}
