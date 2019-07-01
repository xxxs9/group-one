package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.CollectionDao;
import com.heeexy.example.dao.ExternalUserDao;
import com.heeexy.example.dao.UserAttentionDao;
import com.heeexy.example.service.ExternalUserService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.heeexy.example.util.constants.ErrorEnum.E_400;
import static com.heeexy.example.util.constants.ErrorEnum.E_503;

/**
 * @author: liminhao
 * @date: 2019-06-18 13:06
 * @description: 用户/权限
 * @version:
 */
@Service
public class ExternalUserServiceImpl implements ExternalUserService {
    @Autowired
    ExternalUserDao userDao;

    @Autowired
    UserAttentionDao userAttentionDao;

    @Autowired
    CollectionDao collectionDao;

    /**
     * 用户列表
     * @param jsonObject
     * @return JSONObject
     */
    @Override
    public JSONObject getUser(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = userDao.countUser(jsonObject);

        List<JSONObject> list = userDao.getUser(jsonObject);
        for (JSONObject object : list) {
            object.put("fansCount",Integer.parseInt(object.getString("fansCount")));
        }
        String reg = "[a-zA-Z0-9\\u0391-\\uFFE5]+$";
        boolean flag = false;
        String querykey = jsonObject.getString("querykey");
        if(!querykey.equals("")){
            if(querykey.matches(reg)){
                System.out.println(jsonObject.getString("querykey"));
                flag = true;
            }else{
                System.out.println(jsonObject.getString("querykey"));
            }
        }else {
            flag = true;
        }
        if(flag){
            return CommonUtil.successPage(jsonObject, list, count);
        }else {
            return CommonUtil.errorJson(E_503);
        }
    }

    /**
     * 添加用户
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject addUser(JSONObject jsonObject) {
        int exist = userDao.queryExistUUID(jsonObject);
        if (exist > 0) {
            return CommonUtil.errorJson(ErrorEnum.E_10009);
        }
        userDao.addUser(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 修改用户
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject updateFansOffset(JSONObject jsonObject) {
//        System.out.println(jsonObject.getString("fansOffset"));
//        System.out.println(jsonObject.getString("uuId"));
        String reg = "^\\d{1,6}$";
        if(jsonObject.getString("fansOffset").matches(reg)){
            userDao.updateFansOffset(jsonObject);
            return CommonUtil.successJson();
        }else {
            return CommonUtil.errorJson(E_503);
        }
    }


    /**
     * 获取用户权限
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject getUserPermission(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = userDao.countByUnionId(jsonObject);
        List<JSONObject> list = userDao.getUserPermission(jsonObject);
        String reg = "[a-zA-Z0-9\\u0391-\\uFFE5]+$";
        String querykey = jsonObject.getString("querykey");
        boolean flag = false;
        if(!querykey.equals("")){

            if(jsonObject.getString("querykey").matches(reg)){
                System.out.println(jsonObject.getString("querykey"));
                flag = true;
            }else {
                System.out.println(jsonObject.getString("querykey"));
            }
        }else {
            flag = true;
        }
        if(flag){
            return CommonUtil.successPage(jsonObject, list, count);
        }else {
            return CommonUtil.errorJson(E_503);
        }
    }

    /**
     * 修改用户的权限状态
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject updatePermission(JSONObject jsonObject) {
//        JSONObject jsonObject1 = new JSONObject();
//        jsonObject1.put("epermissionList",jsonObject.getJSONArray("epermissionList"));
//        jsonObject.put("epermissions",userDao.getPermIdByName(jsonObject1));
//        int i = userDao.removePermission(jsonObject);
//        List<String> epermissionList = (List<String>) jsonObject.get("epermissionList");
//        List<JSONObject> permIdByName = userDao.getPermIdByName(epermissionList);
//        List<Integer> epermissions = new ArrayList<>();
//        for(int i=0;i<permIdByName.size();i++){
//
//            epermissions.add(permIdByName.get(i).getInteger("id"));
//
//        }
        List<Integer> epermissions = (List<Integer>) jsonObject.get("epermissionList");
        System.out.println(epermissions);
        Integer uuId = jsonObject.getInteger("uuId");

        //判断该用户的权限列表长度
        if(userDao.getPermByUUID(jsonObject).size()==0){
            //若用户不拥有权限，则给用户添加所有权限
            userDao.addPermission(uuId,epermissions);

        }else if(epermissions.size()==userDao.getPerm(jsonObject).size()){
            //若前台传回来的权限列表长度与所有权限列表长度相等，则将所有权限置为有效
            userDao.refreshPermissionStatus(jsonObject);

        }
//        else if(epermissions.size()==userDao.getPermByUUID(jsonObject).size()){
//            //若前台传回来的权限列表长度与用户拥有的权限列表长度相同，则用户权限并未改动，直接返回
//            return CommonUtil.successJson();
//
//        }
        else if(epermissions.size()==0){
            userDao.removeUserAllPermission(jsonObject);
        }
        else {

            userDao.updatePermission(uuId,epermissions);
            userDao.updatePermission2(uuId,epermissions);

        }
        return CommonUtil.successJson();
    }

    /**
     * 修改用户被禁用的权限状态 delete_status改为'1'
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject refreshPermissionStatus(JSONObject jsonObject) {
        userDao.refreshPermissionStatus(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 给用户添加权限
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject addPermission(JSONObject jsonObject) {
        List<Integer> epermissions = new ArrayList<>();
        //给权限列表中添加所有权限的权限码
        epermissions.add(101);
        epermissions.add(102);
        epermissions.add(103);
        epermissions.add(104);
        Integer uuId = jsonObject.getInteger("uuId");
        userDao.addPermission(uuId,epermissions);
        return CommonUtil.successJson();
    }

    /**
     * 获取用户的粉丝列表信息
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject getFans(JSONObject jsonObject) {
        List<JSONObject> fansUUIDByUUID = userDao.getFansUUIDByUUID(jsonObject.getInteger("uuId"));
        List<JSONObject> list = new ArrayList<>();
        for(JSONObject object:fansUUIDByUUID){
//            System.out.println(object);
            Integer fansuuId = object.getInteger("from_user_uuid");
//            System.out.println(fansuuId);
            JSONObject userById = userDao.findUserById(fansuuId);
//            System.out.println(userById);
            list.add(userById);
        }

        return CommonUtil.successPage(list);
    }

    /**
     * 查询权限列表
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject getPerm(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        List<JSONObject> allPerm = userDao.getPerm(jsonObject);
        List<JSONObject> userPerm = userDao.getPermByUUID(jsonObject);

        for (JSONObject allperm : allPerm) {
            Boolean flag = false;
            for (JSONObject userperm : userPerm) {
                String permId = allperm.getString("permId");
                String string = userperm.getString("permId");
                if(permId.equals(string)){
                    flag = true;
                    break;
                }
            }
            if(flag == true){
                allperm.put("state",1);
            }else {
                allperm.put("state",0);
            }
        }

        return CommonUtil.successPage(allPerm);
    }

    @Override
    public JSONObject getPostByUUID(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject getPostIdByLike(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject findIconById(JSONObject jsonObject) {
        return null;
    }


    /**
     * 获取用户信息(发帖数量，关注数量，粉丝数量，点赞数量，收藏数量，头像、昵称)
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject getMyself(JSONObject jsonObject) {
        Integer uuId = jsonObject.getInteger("uuId");
        int myrelease = userDao.countPostByUUID(jsonObject);//发帖数量
        int myattention = userAttentionDao.countIdolByUUID(jsonObject);//关注数量
        int myfans = userAttentionDao.countFansByUUID(jsonObject);//粉丝数量
        int mygood = userDao.countGoodByUUID(jsonObject);//点赞数量
        jsonObject.put("userId",uuId);
        int mycollention = collectionDao.countByUserId(jsonObject);//收藏数量
        JSONObject myicon = userDao.findIconById(jsonObject);//头像、昵称

        JSONObject myself = new JSONObject();
        myself.put("myrelease",myrelease);
        myself.put("myattention",myattention);
        myself.put("myfans",myfans);
        myself.put("mygood",mygood);
        myself.put("mycollention",mycollention);
        myself.put("myicon",myicon);

        return CommonUtil.successJson(myself);
    }


}

