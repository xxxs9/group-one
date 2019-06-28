package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.SortService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    String string = new String();
    @RequiresPermissions("post:list")
    @GetMapping("/listSort")
    public JSONObject listSort(HttpServletRequest request) {
        return sortService.listSort(CommonUtil.request2Json(request));
    }

    @RequiresPermissions("post:add")
    @PostMapping("/addSort")
    public JSONObject addSort(@RequestBody JSONObject requestJson) {
        requestJson.put("imageUrl",string);
        CommonUtil.hasAllRequired(requestJson, "sortname,imageUrl");
        return sortService.addSort(requestJson);
    }
    @RequiresPermissions("post:update")
    @PostMapping("/updateSort")
    public JSONObject updateSort(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,sortname,imageUrl");
        return sortService.updateSort(requestJson);
    }
    @RequiresPermissions("post:delete")
    @PostMapping("/deleteSort")
    public JSONObject deleteSort(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        return sortService.deleteSort(requestJson);
    }
    @RequestMapping("/upload")
    public Map imgUpload(HttpServletRequest request, MultipartHttpServletRequest multiReq) throws IOException {
        Map<String,Object> map = new HashMap<>();
        MultipartFile file = multiReq.getFile("file");
        String originalFilename = file.getOriginalFilename();
        String desFilePath = "G:" + File.separator + "peixuntool"
                + File.separator + "IDE-workspace"
                + File.separator + "group-one"
                + File.separator + "vue"
                + File.separator + "src"
                + File.separator + "assets"
                + File.separator + "/" + originalFilename;
        File localFile = new File(desFilePath);
        String srcUrl = "http://localhost:9520/static/img/" + originalFilename;
        string = srcUrl;
        if(!localFile.exists()){
            localFile.createNewFile();
            file.transferTo(localFile);
        }
        map.put("code",0);
        map.getOrDefault("msg","上传成功");
        map.put("url",srcUrl);
        return map;


    }
}
