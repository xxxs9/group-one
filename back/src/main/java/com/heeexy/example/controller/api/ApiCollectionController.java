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
 * @description 用户收藏接口
 * @data 2019-06-28 09:50
 */

@RestController
@RequestMapping("/api/collection")
public class ApiCollectionController {
    @Autowired
    CollectionServiceImpl collectionService;

    @Autowired
    PostServiceImpl postService;

    /**
     * 前台用户收藏列表接口
     */
    @RequestMapping("/collectionList")
    public JSONObject collectionList(HttpServletRequest request) {
//        CommonUtil.request2Json(request).put("",111 );
        return collectionService.getByUserId(CommonUtil.request2Json(request));
    }

    /**
     * 前台删除收藏中的帖子接口
     */
    @RequestMapping("/deleteCollection")
    public JSONObject deleteCollection(@RequestBody JSONObject requestJson){
        return collectionService.deleteCollection(requestJson);
    }

    /**
     * 前台添加收藏接口
     */
    @RequestMapping("/addComment")
    public JSONObject addComment(@RequestBody JSONObject requestJson){
        return collectionService.addCollection(requestJson);
    }
}
