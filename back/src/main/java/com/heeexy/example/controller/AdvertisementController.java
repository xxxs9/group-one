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
 * @description
 * @data 2019-06-19 14:50
 */
@RestController
@RequestMapping("/src")
public class AdvertisementController {
    @Autowired
    AdvertisementServiceImpl advertisementService;

    /**
     * 后台广告列表接口
     */
    @RequestMapping("/list")
    public JSONObject listAdvertisement(HttpServletRequest request){
        return advertisementService.listAllAdvertisement(CommonUtil.request2Json(request));
    }

    /**
     * 后台上传广告图片接口
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
     * 后台增加广告接口
     */
    @RequestMapping("/add")
    public JSONObject addAdvertisement (@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "advertisementType");
        JSONObject jsonObject = advertisementService.addAdvertisement(requestJson);
        return CommonUtil.successJson();
    }

    /**
     * 后台移除广告接口
     */
    @PostMapping("/remove")
    public JSONObject removeAdvertisement(@RequestBody JSONObject requestJson) {
        return advertisementService.removeAdvertisement(requestJson);
    }

    /**
     * 后台修改广告接口
     */
    @PostMapping("/update")
    public JSONObject updateAdvertisement(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, " advertisementType");
        return advertisementService.updateAdvertisement(requestJson);
    }

    /**
     * 后台删除本地文件夹图片接口
     */
    @PostMapping("/delete")
    public JSONObject delete (@RequestBody JSONObject jsonObject) {
        File file = new File(jsonObject.getString("desFilePath"));
        file.delete();
        return CommonUtil.successJson();
    }
}
