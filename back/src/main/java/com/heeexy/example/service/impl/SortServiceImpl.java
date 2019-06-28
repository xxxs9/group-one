package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.SortDao;
import com.heeexy.example.service.SortService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: linchen
 * @description:
 * @date: 2019/6/18
 * @Version:
 */
@Service
public class SortServiceImpl implements SortService {
    @Autowired
    private SortDao sortDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addSort(JSONObject jsonObject) {
        List<JSONObject> list = sortDao.getSortByName(jsonObject);
        if(list.size()>0){
            return CommonUtil.errorJson(ErrorEnum.E_10012);
        }else{
            sortDao.addSort(jsonObject);
            return CommonUtil.successJson();
        }

    }

    @Override
    public JSONObject listSort(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = sortDao.countSort(jsonObject);
        List<JSONObject> list = sortDao.listSort(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject listSortName(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = sortDao.countSort(jsonObject);
        List<JSONObject> list = sortDao.listSortName(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateSort(JSONObject jsonObject) {
        List<JSONObject> list = sortDao.getSortById(jsonObject);
        if(list.size()>0){
            return CommonUtil.errorJson(ErrorEnum.E_10012);
        }else{
            sortDao.updateSort(jsonObject);
            return CommonUtil.successJson();
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject deleteSort(JSONObject jsonObject) {
        sortDao.deleteSort(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject recoverySort(JSONObject jsonObject) {
        sortDao.recoverySort(jsonObject);

        return  CommonUtil.successJson();
    }


}
