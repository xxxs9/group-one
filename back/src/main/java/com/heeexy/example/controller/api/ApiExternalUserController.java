package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ExternalUserDao;
import com.heeexy.example.service.ExternalUserService;
import com.heeexy.example.service.PostService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: liminhao
 * @date: 2019-06-28 10:16
 * @description: 前台用户接口
 * @version:
 */
@Controller
@RequestMapping("/api/user")
public class ApiExternalUserController {

    @Autowired
    ExternalUserService externalUserService;



    /**
     * 获取用户信息(发帖数量，关注数量，粉丝数量，点赞数量，收藏数量，头像、昵称)
     * @param request
     * @return
     */
    @GetMapping("/myself")
    public JSONObject getMyself(HttpServletRequest request){

        return externalUserService.getMyself(CommonUtil.request2Json(request));
    }

    @GetMapping("/others")
    public JSONObject getOthers(JSONObject jsonObject){
//        externalUserService.getMyself()
//        postService.getPostListApi(CommonUtil.request2Json(request));
        return null;
    }




}

