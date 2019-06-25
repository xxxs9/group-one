package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ExternalUserDao;
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

    @Override
    public JSONObject addUser(JSONObject jsonObject) {
        int exist = userDao.queryExistUUID(jsonObject);
        if (exist > 0) {
            return CommonUtil.errorJson(ErrorEnum.E_10009);
        }
        userDao.addUser(jsonObject);
        return CommonUtil.successJson();
    }

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

    @Override
    public JSONObject removePermission(JSONObject jsonObject) {
//        JSONObject jsonObject1 = new JSONObject();
//        jsonObject1.put("epermissionList",jsonObject.getJSONArray("epermissionList"));
//        jsonObject.put("epermissions",userDao.getPermIdByName(jsonObject1));
//        int i = userDao.removePermission(jsonObject);
        List<String> epermissionList = (List<String>) jsonObject.get("epermissionList");
        List<JSONObject> permIdByName = userDao.getPermIdByName(epermissionList);
        List<Integer> epermissions = new ArrayList<>();
        for(int i=0;i<permIdByName.size();i++){

            epermissions.add(permIdByName.get(i).getInteger("id"));

        }
        Integer uuId = jsonObject.getInteger("uuId");
        userDao.removePermission(uuId,epermissions);

        return CommonUtil.successJson();
    }

    @Override
    public JSONObject refreshPermissionStatus(JSONObject jsonObject) {
        userDao.refreshPermissionStatus(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addPermission(JSONObject jsonObject) {
        userDao.addPermission(jsonObject);
        return CommonUtil.successJson();
    }

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


}

