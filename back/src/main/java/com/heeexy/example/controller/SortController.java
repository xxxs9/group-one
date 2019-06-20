package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.SortService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: linchen
 * @description:
 * @date: 2019/6/18
 * @Version:
 */
@RestController
@RequestMapping("/sort")
public class SortController {
    @Autowired
    private SortService sortService;

    @GetMapping("/listSort")
    public JSONObject listSort(HttpServletRequest request) {
        return sortService.listSort(CommonUtil.request2Json(request));
    }
}
