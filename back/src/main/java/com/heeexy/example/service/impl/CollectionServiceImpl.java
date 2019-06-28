package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.CollectionDao;
import com.heeexy.example.service.CollectionService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 用户收藏帖子列表
     */
    @Override
    public JSONObject getByUserId(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = collectionDao.getByUserIdCount(jsonObject);
        List<JSONObject> list = collectionDao.getByUserId(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 用户添加帖子到收藏
     */
    @Override
    public JSONObject addCollection(JSONObject jsonObject) {
        //根据帖子id判断此贴是否已被收藏
        int exist = collectionDao.getByPostIdCount(jsonObject);
        if (exist>0) {
            return CommonUtil.errorJson(ErrorEnum.E_10010);
        }else {
            int count = collectionDao.getByUserIdCount(jsonObject);
            if (count>200) {
                return CommonUtil.errorJson(ErrorEnum.E_10011);
            }
        }
        collectionDao.addCollection(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 用户移除收藏中的帖子
     */
    @Override
    public JSONObject deleteCollection(JSONObject jsonObject) {
        //根据用户id去查总数
        collectionDao.deleteCollection(jsonObject);
        return CommonUtil.successJson();
    }
}
