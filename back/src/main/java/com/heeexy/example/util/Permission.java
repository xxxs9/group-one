package com.heeexy.example.util;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ExternalUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author L-YX
 * @version 1.0
 * @description
 * @data 2019-07-04 15:11
 */
@Component
public class Permission {

    @Autowired
    ExternalUserDao externalUserDao;

    public boolean getPermission (JSONObject jsonObject){
        int exist = externalUserDao.queryExistUUID(jsonObject);
        boolean flag = false;
        //判断用户是否存在
        if (exist!=0){
            //用户存在
            //判断用户是否为登录用户
            if (jsonObject.getInteger("uuId").equals(jsonObject.getInteger("userId"))) {
                //是登录用户
                Integer uuId = jsonObject.getInteger("userId");
                JSONObject userById = externalUserDao.findUserById(uuId);
                //判断该用户是否是游客
                if (userById.getString("unionId") != null) {
                    //不是游客
                    flag = true;
                }
            }
        }
        return flag;
    }
}
