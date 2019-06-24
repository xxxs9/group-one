package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.TagSevice;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: linchen
 * @description:
 * @date: 2019/6/21
 * @Version:
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagSevice tagSevice;

    @GetMapping("/listTag")
    public JSONObject listArticle(HttpServletRequest request) {
        System.out.println(tagSevice.listTag(CommonUtil.request2Json(request)));
        return tagSevice.listTag(CommonUtil.request2Json(request));
    }

}
