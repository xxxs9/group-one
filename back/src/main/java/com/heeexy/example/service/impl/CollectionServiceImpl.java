package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.CollectionDao;
import com.heeexy.example.dao.ExternalUserDao;
import com.heeexy.example.dao.PostDao;
import com.heeexy.example.dao.UserDao;
import com.heeexy.example.service.CollectionService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author L-YX
 * @description 收藏逻辑处理实现方法
 * @data 2019-06-18 13:48
 */
@Service
public class CollectionServiceImpl implements CollectionService {
    @Autowired
    private CollectionDao collectionDao;

    @Autowired
    private ExternalUserDao externalUserDao;

    @Autowired
    PostServiceImpl postService;

    /**
     * 用户收藏帖子列表
     */
    @Override
    public JSONObject getByUserId(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
//        int exist = externalUserDao.queryExistUUID(jsonObject);
//        //判断用户是否存在
//        if (exist==0){
//            //用户不存在
//        }else {
//            //判断用户是否为登录用户
            //判断该用户是否是游客
//        JSONObject userById = externalUserDao.findUserById(jsonObject.getInteger("uuId"));
//        if (userById.getInteger("unionId") != null) {
//
//        }
//        }
        int count = collectionDao.getByUserIdCount(jsonObject);
        List<JSONObject> list = collectionDao.getByUserId(jsonObject);
        List<JSONObject> postIdByUserId = collectionDao.getPostIdByUserId(jsonObject);
        jsonObject.put("postIdList", postIdByUserId);
        List<JSONObject> postListApi = postService.getPostListApi(jsonObject);
        JSONObject collectionList = new JSONObject();
        collectionList.put("postListApi", postListApi);
        collectionList.put("collectionList",list );
        return CommonUtil.successJson(collectionList);
    }

    /**
     * 用户添加帖子到收藏
     * @param jsonObject (key:userId,postId)
     * @return JSONObject
     */
    @Override
    public JSONObject addCollection(JSONObject jsonObject) {
        //根据帖子id查询总数
        int exist = collectionDao.getByPostIdCount(jsonObject);
        //根据总数判断该帖子是否被收藏
        if (exist>0) {
            //exist>0返回提示该帖子已被收藏
            return CommonUtil.errorJson(ErrorEnum.E_10010);
        }else {
            //根据用户id查询总数
            int count = collectionDao.getByUserIdCount(jsonObject);
            //根据总数判断是否超过最大收藏数
            if (count>200) {
                //count>200返回提示已超过最大收藏数
                return CommonUtil.errorJson(ErrorEnum.E_10011);
            }
        }
        collectionDao.addCollection(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 根据用户需求来通过收藏id移除收藏里的帖子
     * @param jsonObject (key:postId,userId)
     * @return JSONObject
     */
    @Override
    public JSONObject deleteCollection(JSONObject jsonObject) {
        //根据用户id去查总数
        collectionDao.deleteCollection(jsonObject);
        return CommonUtil.successJson();
    }
}
