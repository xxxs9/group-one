package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.impl.AdvertisementServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author L-YX
 * @version 1.0
 * @description 广告接口
 * @data 2019-06-28 09:43
 */
@RestController
@RequestMapping("/home")
public class ApiAdvertisementController {
    @Autowired
    AdvertisementServiceImpl advertisementService;

    /**
     * 前台广告轮播接口
     */
    @GetMapping("/swiper")
    public JSONObject advertisementList(HttpServletRequest request){
        return advertisementService.advertisementList(CommonUtil.request2Json(request));
    }

}
