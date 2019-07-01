package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.impl.AdvertisementServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * @author L-YX
 * @version 1.0
 * @description 后台广告管理
 * @data 2019-06-19 14:50
 */
@RestController
@RequestMapping("/src")
public class AdvertisementController {
    @Autowired
    AdvertisementServiceImpl advertisementService;

    /**
     * 查询广告列表(当广告标题为空时无条件查询全部广告列表，反之根据广告标题模糊查询)接口
     * @param request
     * @return JSONObject
     */
    @RequestMapping("/list")
    public JSONObject listAdvertisement(HttpServletRequest request){
        return advertisementService.listAllAdvertisement(CommonUtil.request2Json(request));
    }

    /**
     * 后台上传广告图片接口
     * @param req,multiReq
     * @return Map
     */
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
        localFile.createNewFile();
        file.transferTo(localFile);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(desFilePath));
        Image bi = ImageIO.read(in);
        BufferedImage tag = new BufferedImage(414, 82, BufferedImage.TYPE_INT_RGB);
        tag.getGraphics().drawImage(bi, 0, 0,414, 82, null);
        localFile.delete();
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desFilePath));
        ImageIO.write(tag, "PNG",out);
        in.close();
        out.close();
        map.put("code", 0);
        map.put("msg", "上传成功");
        map.put("desFilePath", desFilePath);
        map.put("url", srcUrl);
        return map;
    }

    /**
     * 添加广告接口
     * @param requestJson
     * @return JSONObject
     */
    @RequestMapping("/add")
    public JSONObject addAdvertisement (@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "advertisementType");
        JSONObject jsonObject = advertisementService.addAdvertisement(requestJson);
        return CommonUtil.successJson();
    }

    /**
     * 后台移除广告接口
     * @param requestJson
     * @return JSONObject
     */
    @PostMapping("/remove")
    public JSONObject removeAdvertisement(@RequestBody JSONObject requestJson) {
        return advertisementService.removeAdvertisement(requestJson);
    }

    /**
     * 后台修改广告接口
     * @param requestJson
     * @return JSONObject
     */
    @PostMapping("/update")
    public JSONObject updateAdvertisement(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, " advertisementType");
        return advertisementService.updateAdvertisement(requestJson);
    }

    /**
     * 后台删除本地文件夹图片接口
     * @param jsonObject
     * @return JSONObject
     */
    @PostMapping("/delete")
    public JSONObject delete (@RequestBody JSONObject jsonObject) {
        File file = new File(jsonObject.getString("desFilePath"));
        file.delete();
        return CommonUtil.successJson();
    }
}
