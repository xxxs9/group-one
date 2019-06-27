package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.impl.AdvertisementServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author L-YX
 * @version 1.0
 * @description
 * @data 2019-06-19 14:50
 */
@RestController
@RequestMapping("/src")
public class AdvertisementController {
    @Autowired
    AdvertisementServiceImpl advertisementService;
    List<String> list = new ArrayList<>();


    @RequestMapping("/list")
    public JSONObject listAdvertisement(HttpServletRequest request){
        return advertisementService.listAllAdvertisement(CommonUtil.request2Json(request));
    }

    @RequestMapping(value = "/upload")
    public Map imgUpload(HttpServletRequest req, MultipartHttpServletRequest multiReq) throws IOException {
        Map<String,Object> map = new HashMap<>();
        MultipartFile file = multiReq.getFile("file");
        String originalFilename = file.getOriginalFilename();
        String temp = UUID.randomUUID().toString();
        String desFilePath =
                "D:" + File.separator+"static"
                        + "/" + temp
                        +originalFilename;
        File localFile  = new File(desFilePath);
        String srcUrl = desFilePath.replaceFirst("D:\\\\", "http://localhost:8080/");
        list.add(srcUrl);
        localFile.createNewFile();
        file.transferTo(localFile);
        map.put("code", 0);
        map.put("msg", "上传成功");
        map.put("desFilePath", desFilePath);
        map.put("url", srcUrl);
        return map;
    }

    @RequestMapping("/add")
    public JSONObject addAdvertisement (@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "advertisementType");
        requestJson.put("src",list);
        JSONObject jsonObject = advertisementService.addAdvertisement(requestJson);
        list.clear();
        return CommonUtil.successJson();
    }

    @PostMapping("/remove")
    public JSONObject removeAdvertisement(@RequestBody JSONObject requestJson) {
        System.out.println(111);
        return advertisementService.removeAdvertisement(requestJson);
    }

    @PostMapping("/update")
    public JSONObject updateAdvertisement(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, " advertisementType,srcUrl");
        if (list.size()!=0) {
            for (String s : list) {
                String srcUrl = s;
                requestJson.put("srcUrl",srcUrl );
            }
        }
        return advertisementService.updateAdvertisement(requestJson);
    }

    @PostMapping("/delete")
    public void delete (@RequestBody JSONObject jsonObject) {
        File file = new File(jsonObject.getString("desFilePath"));
        file.delete();
        list.remove(jsonObject.getString("src"));
    }

}
