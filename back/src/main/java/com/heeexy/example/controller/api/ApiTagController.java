package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.TagSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: linchen
 * @description:
 * @date: 2019/7/9
 * @Version:
 */
@RestController
@RequestMapping("/api/tag")
public class ApiTagController {
    @Autowired
    private TagSevice tagSevice;

    @PostMapping("/getTags")
    public JSONObject getTag( @RequestBody JSONObject requestJson) {
        return tagSevice.getAllTags(requestJson);
    }
}
