package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.SortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: linchen
 * @description:
 * @date: 2019/7/4
 * @Version:
 */
@RestController
@RequestMapping("/api/types")
public class ApiSortController {
    @Autowired
    private SortService sortService;


    @PostMapping("/getBase.api")
    public JSONObject getSortType(@RequestBody JSONObject requestJson) {
//        CommonUtil.hasAllRequired(requestJson, "postId,userId");
        return sortService.getSortType(requestJson);
    }

}
