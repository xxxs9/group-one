package com.heeexy.example.dao;

import com.alibaba.fastjson.JSON;
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
     * @param jsonObject key: querykey,beforeDate,afterDate
     * @return  List
     * @description 查询用户列表
    */
    public List<JSONObject> getUser(JSONObject jsonObject);

    /**
     * @author liminhao
     * @date 2019-06-18 09:09
     * @param jsonObject
     * @return
     * @description 查询用户数量
    */
    public int countUser(JSONObject jsonObject);

    /**
     * @author liminhao
     * @date 2019-06-18 09:14
     * @param jsonObject key:uuId,unionId,username,iconUrl,mobile,sex
     * @return
     * @description 添加用户
    */
    public int addUser(JSONObject jsonObject);

    /**
     * @author liminhao
     * @date 2019-06-18 09:15
     * @param jsonObject key:fansOffset,uuId
     * @return
     * @description 修改用户
    */
    public int updateFansOffset(JSONObject jsonObject);

    /**
     * @author liminhao
     * @date 2019-06-18 09:16
     * @param jsonObject key:querykey
     * @return
     * @description 获取用户所有权限
    */
    public List<JSONObject> getUserPermission(JSONObject jsonObject);

    /**
     * @author liminhao
     * @date 2019-06-18 09:30
     * @param uuId,epermissions key:uuId,permId
     * @return
     * @description 将用户曾经拥有而修改为不再拥有的权限 delete_status改为'2'
    */
    public int updatePermission(@Param("uuId")Integer uuId,@Param("epermissions")List<Integer> epermissions);

    /**
     * @author liminhao
     * @date 2019-06-18 09:30
     * @param uuId,epermissions key:uuId,permId
     * @return
     * @description 将用户被禁用的权限恢复为拥有的权限 delete_status改为'1'
     */
    public int updatePermission2(@Param("uuId")Integer uuId,@Param("epermissions")List<Integer> epermissions);
    /**
     * @author liminhao
     * @date 2019-06-18 09:33
     * @param uuId,epermissions key:uuId,permId
     * @return
     * @description 给用户添加权限
    */
    public int addPermission(@Param("uuId")Integer uuId,@Param("epermissions")List<Integer> epermissions);

    /**
     * @author liminhao
     * @date 2019-06-18 09:50
     * @param object key:uuId
     * @return
     * @description 恢复用户正常的权限状态 delete_status改为'1'
    */
    public int refreshPermissionStatus(JSONObject object);

    /**
     * @author liminhao
     * @date 2019-06-18 09:51
     * @param jsonObject key:uuId
     * @return
     * @description 移除用户的所有权限
    */
    public int removeUserAllPermission(JSONObject jsonObject);

    /**
     * 校验uuid是否重复
     * @param jsonObject key:uuId
     * @return
     */
    public int queryExistUUID(JSONObject jsonObject);

    /**
     * 根据权限名查询id
     * @param epermissionList
     * @return
     */
    public List<JSONObject> getPermIdByName(@Param("epermissionList") List<String> epermissionList);

    /**
     * 根据uuId查询用户
     * @param uuId
     * @return
     */
    public JSONObject findUserById(@Param("uuId")Integer uuId);

    /**
     * 获取用户的粉丝用户ID
     * @param uuId
     * @return
     */
    public List<JSONObject> getFansUUIDByUUID(@Param("uuId")Integer uuId);

    /**
     * 查询用户数量(拥有unionid)
     * @param jsonObject
     * @return
     */
    public int countByUnionId(JSONObject jsonObject);

    /**
     * 查询权限列表
     * @param jsonObject
     * @return
     */
    public List<JSONObject> getPerm(JSONObject jsonObject);

    /**
     * 根据uuid查询该用户拥有的权限id
     * @param jsonObject key:uuId
     * @return
     */
    public List<JSONObject> getPermByUUID(JSONObject jsonObject);

    /**
     * 根据UUID获取该用户所发的帖子id
     * @param jsonObject key:uuId
     * @return
     */
    List<JSONObject> getPostByUUID(JSONObject jsonObject);

    /**
     * 获取用户关注的用户uuid
     * @param jsonObject key:uuId
     * @return
     */
    List<JSONObject> getMyIdolId(JSONObject jsonObject);

    /**
     * 统计用户发的帖子数
     * @param jsonObject key:uuId
     * @return
     */
    int countPostByUUID(JSONObject jsonObject);

    /**
     * 获取用户点赞过的帖子id
     * @param jsonObject key:uuId
     * @return
     */
    List<JSONObject> getPostIdByLike(JSONObject jsonObject);

    /**
     * 根据uuId查询用户头像
     * @param jsonObject key:uuId
     * @return
     */
    JSONObject findIconById(JSONObject jsonObject);

    /**
     * 统计用户点赞过的帖子数量
     * @param jsonObject key:uuId
     * @return
     */
    int countGoodByUUID(JSONObject jsonObject);
}
