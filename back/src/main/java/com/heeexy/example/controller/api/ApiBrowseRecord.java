package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.BrowseRecordService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: liminhao
 * @date: 2019-07-02 15:36
 * @description:
 * @version:
 */
@RestController
@RequestMapping("/api/record")
public class ApiBrowseRecord {

    @Autowired
    BrowseRecordService browseRecordService;

    /**
     * 新增浏览记录
     * @param requestJson
     * @return
     */
    @PostMapping("/add")
    public JSONObject addRecord(@RequestBody JSONObject requestJson){
        requestJson.put("uuId",requestJson.getInteger("userId"));
        return browseRecordService.addRecord(requestJson);
    }

}

