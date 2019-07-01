package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.BrowseRecordService;
import com.heeexy.example.service.ExternalUserService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: liminhao
 * @date: 2019-06-18 13:53
 * @description:
 * @version:
 */
@RestController
@RequestMapping("/euser")
public class ExternalUserController {

    @Autowired
    ExternalUserService service;

    @Autowired
    BrowseRecordService recordService;

    /**
     * 获取用户列表
     * @param request
     * @return
     */
    @RequiresPermissions("euser:list")
    @GetMapping("/userlist")
    public JSONObject listUser(HttpServletRequest request) {
        return service.getUser(CommonUtil.request2Json(request));
    }

    /**
     * 修改粉丝偏移量
     * @param requestJson
     * @return
     */
    @RequiresPermissions("euser:update")
    @PostMapping("/updateFO")
    public JSONObject updateFansOffset(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"fansOffset");
        return service.updateFansOffset(requestJson);
    }

    /**
     * 获取权限列表
     * @param request
     * @return
     */
    @GetMapping("/userPerm")
    public JSONObject listPerm(HttpServletRequest request){
        return service.getUserPermission(CommonUtil.request2Json(request));
    }

    /**
     * 修改用户权限
     * @param requestJson
     * @return
     */
    @RequiresPermissions("euser:update")
    @PostMapping("/updatePerm")
    public JSONObject updatePermission(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"uuId,epermissionList");
        return service.updatePermission(requestJson);
    }

    /**
     * 恢复用户权限
     * @param requestJson
     * @return
     */
    @PostMapping("/refreshPerm")
    public JSONObject refreshPermission(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"uuId");
        return service.refreshPermissionStatus(requestJson);
    }

    /**
     * 获取粉丝列表
     * @param request
     * @return
     */
    @RequiresPermissions("euser:list")
    @GetMapping("/fanslist")
    public JSONObject getFans(HttpServletRequest request){
        return service.getFans(CommonUtil.request2Json(request));
    }

    /**
     *
     * @param request
     * @return
     */
    @GetMapping("/perms")
    public JSONObject getPerm(HttpServletRequest request){
        return service.getPerm(CommonUtil.request2Json(request));
    }

    /**
     * 获取浏览记录
     * @param request
     * @return
     */
    @RequiresPermissions("euser:list")
    @GetMapping("/recordlist")
    public JSONObject getRecord(HttpServletRequest request){
        JSONObject records = recordService.getRecords(CommonUtil.request2Json(request));
//        System.out.println(records.get("list"));
        return recordService.getRecords(CommonUtil.request2Json(request));

    }
}

