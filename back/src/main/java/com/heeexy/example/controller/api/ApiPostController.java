package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.util.CommonUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/28 9:48
 * @description：小程序的接口
 * @version: 0.1$
 */
@RestController
@RequestMapping("/")
public class ApiPostController {

//    @PostMapping("/postDetail")
//    public JSONObject postDetail(@RequestBody JSONObject requestJson) {
//        CommonUtil.hasAllRequired(requestJson, "tid");
//        return articleService.addArticle(requestJson);
//    }
}
