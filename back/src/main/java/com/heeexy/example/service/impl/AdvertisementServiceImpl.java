package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.AdvertisementDao;
import com.heeexy.example.dao.PostDao;
import com.heeexy.example.dao.TagDao;
import com.heeexy.example.service.AdvertisementService;
import com.heeexy.example.util.CommonUtil;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author L-YX
 * @description 广告逻辑处理实现方法
 * @data 2019-06-18 14:32
 */
@Service
public class AdvertisementServiceImpl implements AdvertisementService {
    @Autowired
    private AdvertisementDao advertisementDao;

    @Autowired
    PostDao postDao;

    @Autowired
    TagDao tagDao;

    @Autowired
    PostServiceImpl postService;

    /**
     * 查询广告列表(当广告标题为空时无条件查询全部广告列表，反之根据广告标题模糊查询)
     * @param jsonObject (key:advertisementType)
     * @return JSONObject
     */
    @Override
    public JSONObject listAllAdvertisement(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        List<JSONObject> list = advertisementDao.listAllAdvertisement(jsonObject);
        int count = advertisementDao.countAdvertisement(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 添加广告
     * @param jsonObject (key:advertisementRef,srcUrl,advertisementType,advertisementStatus)
     * @return JSONObject
     */
    @Override
    public JSONObject addAdvertisement(JSONObject jsonObject) {
        JSONArray src = jsonObject.getJSONArray("imgList");
        for (Object o : src) {
            jsonObject.put("advertisementStatus",1 );
            jsonObject.put("srcUrl",o );
            advertisementDao.addAdvertisement(jsonObject);
        }
        return CommonUtil.successJson();
    }

    /**
     * 修改广告（修改客户需求图片链接，图片路径和图片标题）
     * @param jsonObject (key:advertisementRef,srcUrl,advertisementType,advertisementId)
     * @return JSONObject
     */
    @Override
    public JSONObject updateAdvertisement(JSONObject jsonObject) {
        JSONArray src = jsonObject.getJSONArray("imgList");
        String url = jsonObject.getString("srcUrl");
        String desFilePath = url.replaceFirst("http://localhost:8080/","D://" );
        File localFile  = new File(desFilePath);
        localFile.delete();
        if (src.size()!=0){
            for (Object o : src) {
                jsonObject.put("srcUrl",o );
            }
        }
        advertisementDao.updateAdvertisement(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 修改广告状态，决定播放与否
     * @param jsonObject (key:advertisementStatus,advertisementId)
     * @return JSONObject
     */
    @Override
    public JSONObject removeAdvertisement(JSONObject jsonObject) {
        advertisementDao.removeAdvertisement(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 根据广告状态查询显示广告
     * @param jsonObject (无有用参数)
     * @return JSONObject
     */
    @Override
    public JSONObject advertisementList(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        List<JSONObject> list = advertisementDao.advertisementList();
        int count = 6;
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject search(JSONObject jsonObject) {
        List<JSONObject> listTag = tagDao.getTagTextByTagId(jsonObject);
        List<Integer> queryPostTagId = new ArrayList<>();
        for (JSONObject object : listTag) {
            queryPostTagId.add(object.getInteger("tag_id")) ;
        }
        jsonObject.put("queryPostTagId",queryPostTagId );
        List<Object> postIdListApi = new ArrayList<>();
        List<JSONObject> postIdList = postDao.listPostIdByTag(jsonObject);
        List<JSONObject> id = postDao.getPostTextByPostId(jsonObject);
        for (JSONObject object : id) {
            postIdListApi.add(object.getInteger("post_id"));
        }
        for (JSONObject object : postIdList) {
            postIdListApi.add(object.getInteger("postId"));
        }
        List<Object> objects = postIdListApi.stream().distinct().collect(Collectors.toList());
        JSONObject searchList = new JSONObject();
        List<JSONObject> postDetailList = new ArrayList<>();
        for (Object object : objects) {
           jsonObject.put("postId", object);
            JSONObject search = postDao.queryPostById(jsonObject);
            postDetailList.add(search);
        }
        searchList.put("searchDetail",postDetailList );
        return CommonUtil.successJson(searchList);
    }
}
