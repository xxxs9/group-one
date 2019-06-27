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
 * @description
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
        int count = collectionDao.countByUserId(jsonObject);
        List<JSONObject> list = collectionDao.getByUserId(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 用户添加帖子到收藏
     */
    @Override
    public JSONObject addCollection(JSONObject jsonObject) {
        //根据帖子id判断此贴是否已被收藏
        int exist = collectionDao.getByPostId(jsonObject);
        if (exist>0) {
            return CommonUtil.errorJson(ErrorEnum.E_10010);
        }else {
            int count = collectionDao.getByCollectionCount(jsonObject);
            if (count==0) {
                count = 1;
            }else if (count>200) {
                return CommonUtil.errorJson(ErrorEnum.E_10011);
            }
            count = count+1;
            jsonObject.put("collectionCount",count );
        }
        jsonObject.put("collectionState",1 );
        collectionDao.addCollection(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 用户移除收藏中的帖子
     */
    @Override
    public JSONObject removeCollection(JSONObject jsonObject) {
        //根据用户id去查总数
        int count = collectionDao.getByCollectionCount(jsonObject);
        jsonObject.put("collectionCount",count-1 );
        collectionDao.removeCollection(jsonObject);
        return CommonUtil.successJson();
    }
}
