package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.impl.CollectionServiceImpl;
import com.heeexy.example.service.impl.PostServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author L-YX
 * @version 1.0
 * @description 前台用户收藏接口
 * @data 2019-06-28 09:50
 */

@RestController
@RequestMapping("/api/collection")
public class ApiCollectionController {
    @Autowired
    CollectionServiceImpl collectionService;

    /**
     * 前台用户收藏列表接口
     * @param requestJson (key:userId)
     * @return JSONObject
     */
    @RequestMapping("/collectionList")
    public JSONObject collectionList(@RequestBody JSONObject requestJson) {
        return collectionService.getByUserId(requestJson);
    }

    /**
     * 前台删除收藏中的帖子接口
     * @param requestJson (key:postId,userId)
     * @return JSONObject
     */
    @RequestMapping("/deleteCollection")
    public JSONObject deleteCollection(@RequestBody JSONObject requestJson){
        return collectionService.deleteCollection(requestJson);
    }

    /**
     * 前台添加收藏接口
     * @param requestJson (key:userId,postId)
     * @return JSONObject
     */
    @RequestMapping("/addComment")
    public JSONObject addComment(@RequestBody JSONObject requestJson){
        return collectionService.addCollection(requestJson);
    }
}
