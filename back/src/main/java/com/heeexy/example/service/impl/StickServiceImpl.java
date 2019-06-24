package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.StickDao;
import com.heeexy.example.service.StickService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/21 21:52
 * @description：板块的Service实现层
 * @version: $
 */
@Service
public class StickServiceImpl implements StickService {

    @Autowired
    private StickDao stickDao;

    @Override
    public JSONObject listStick(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        List<JSONObject> listStick = stickDao.listStick(jsonObject);
        List<JSONObject> listStickByPostId = stickDao.listStickByPostId(jsonObject);
        JSONObject Jobj = new JSONObject();
        Jobj.put("listStick",listStick);
        Jobj.put("listStickByPostId",listStickByPostId);
        return CommonUtil.successJson(Jobj);
    }

    @Override
    public JSONObject listStickByPostId(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        return CommonUtil.successJson(stickDao.listStickByPostId(jsonObject));
    }
}
