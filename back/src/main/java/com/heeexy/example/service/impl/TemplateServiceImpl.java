package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.TemplateDao;
import com.heeexy.example.service.TemplateService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: linchen
 * @description:
 * @date: 2019/6/17
 * @Version:
 */
@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private TemplateDao templateDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addTemplate(JSONObject jsonObject) {

        List<JSONObject> list = templateDao.getTemplateByName(jsonObject);
        if(list.size()>0){
            return CommonUtil.errorJson(ErrorEnum.E_10012);
        }else{
            templateDao.addTemplate(jsonObject);
            return CommonUtil.successJson();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addChatTemplate(JSONObject jsonObject) {
        List<JSONObject> ulist = templateDao.getAllUserId(jsonObject);
        jsonObject.put("ulist",ulist);
        int i = templateDao.addAllUserTemplate(jsonObject);

        return CommonUtil.successJson(ulist);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addWarningTemplate(JSONObject jsonObject) {
        String content =  templateDao.getWarningContentByName();
        String conmentTest = (String) jsonObject.get("commentText");
        String newContent = "您该评论："+conmentTest+","+content;
        jsonObject.put("content",newContent);
        jsonObject.put("userId",jsonObject.get("commentUserId"));
        int i = templateDao.addWarningTemplate(jsonObject);

        return CommonUtil.successJson(jsonObject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addBeginContentByName(JSONObject jsonObject) {
        String beginContent = templateDao.getBeginContentByName();
        jsonObject.put("content",beginContent);

        int i = templateDao.addWarningTemplate(jsonObject);

        return CommonUtil.successJson(jsonObject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addBanContentByName(JSONObject jsonObject) {
        String banContent = templateDao.getBanContentByName();
        jsonObject.put("content",banContent);

        int i = templateDao.addWarningTemplate(jsonObject);

        return CommonUtil.successJson(jsonObject);
    }

    @Override
    public JSONObject listTemplate(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = templateDao.countTemplate(jsonObject);
        List<JSONObject> list = templateDao.listTemplate(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateTemplate(JSONObject jsonObject) {
        List<JSONObject> list = templateDao.getTemplateById(jsonObject);
        if(list.size()>0){
            return CommonUtil.errorJson(ErrorEnum.E_10012);
        }else{
            templateDao.updateTemplate(jsonObject);
            return CommonUtil.successJson();
        }

    }

    @Override
    public JSONObject updateByStatus(JSONObject jsonObject) {
        templateDao.updateByStatus(jsonObject);

        return CommonUtil.successJson();
    }
}
