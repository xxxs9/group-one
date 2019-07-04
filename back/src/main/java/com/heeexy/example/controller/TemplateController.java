package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.TemplateService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: linchen
 * @description:
 * @date: 2019/6/17
 * @Version:
 */
@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    /**
     * 查询文章列表
     */

    @GetMapping("/listTemplate")
    public JSONObject listArticle(HttpServletRequest request) {
        return templateService.listTemplate(CommonUtil.request2Json(request));
    }

    /**
     * 新增文章
     */
    @RequiresPermissions("message:add")
    @PostMapping("/addTemplate")
    public JSONObject addArticle(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "tname,content");
        return templateService.addTemplate(requestJson);
    }
    @PostMapping("/sendTemplate")
    public JSONObject sendAll(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "tname,content");
        return templateService.addChatTemplate(requestJson);
    }
    @PostMapping("/sendWarning")
    public JSONObject sendWarning(@RequestBody JSONObject requestJson) {
        return templateService.addWarningTemplate(requestJson);
    }
    /**
     * 修改文章
     */
    @RequiresPermissions("message:update")
    @PostMapping("/updateTemplate")
    public JSONObject updateArticle(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,content,tname");
        return templateService.updateTemplate(requestJson);
    }
    @RequiresPermissions("message:delete")
    @PostMapping("/deleteTemplate")
    public JSONObject updateByStatus(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        return templateService.updateByStatus(requestJson);
    }
}
