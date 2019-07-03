package com.heeexy.example.controller;


import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.impl.CollectionServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author L-YX
 * @version 1.0
 * @description
 * @data 2019-06-20 16:26
 */
@RestController
@RequestMapping("/collection")
public class CollectionController {
    @Autowired
    CollectionServiceImpl collectionService;

    /**
     * 后台用户收藏列表
     */
    @RequestMapping("/collectionList")
    public JSONObject collectionList(HttpServletRequest request) {
        return collectionService.getByUserId(CommonUtil.request2Json(request));
    }

    /**
     * 删除收藏中的帖子
     */
    @RequestMapping("/deleteCollection")
    public JSONObject deleteCollection(@RequestBody JSONObject requestJson){
        return collectionService.deleteCollection(requestJson);
    }

    /**
     * 添加收藏
     */
    @RequestMapping("/addComment")
    public JSONObject addComment(@RequestBody JSONObject requestJson){
        return collectionService.addCollection(requestJson);
    }
}
