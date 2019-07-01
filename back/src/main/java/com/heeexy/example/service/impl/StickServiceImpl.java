package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONArray;
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

    /**
     * 获取板块列表
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject listStick(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        return CommonUtil.successJson(stickDao.listStick(jsonObject));
    }

    /**
     * 获取帖子的制定板块
     * @param jsonObject 帖子ID
     * @return
     */
    @Override
    public JSONObject listStickByPostId(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        List<JSONObject> listStick = stickDao.listStick(jsonObject);
        List<JSONObject> listStickByPostId = stickDao.listStickById(jsonObject);
        for (JSONObject allStick : listStick) {
            Boolean flag = false;
            for (JSONObject postStick : listStickByPostId) {
                String stickId = allStick.getString("stickId");
                String string = postStick.getString("stickId");
                if(stickId.equals(string)){
                    flag = true;
                    break;
                }
            }
            if(flag == true){
                allStick.put("stickState",1);
            }else {
                allStick.put("stickState",0);
            }
        }
        return CommonUtil.successJson(listStick);
    }


    /**
     * 修改帖子的置顶板块
     * @param jsonObject 帖子ID ，板块ID列表
     * @return
     */
    @Override
    public JSONObject updatePostStick(JSONObject jsonObject) {
        JSONArray valueArr = jsonObject.getJSONArray("values");
        stickDao.deletePostStick(jsonObject);
        for (Object o : valueArr) {
            JSONObject poststickid = new JSONObject();
            poststickid.put("postId",jsonObject.getString("postId"));
            poststickid.put("stickId",o);
            stickDao.addPostStick(poststickid);
        }
        return CommonUtil.successJson();
    }

}
