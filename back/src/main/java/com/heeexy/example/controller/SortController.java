package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.SortService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    @PostMapping("/recoverySort")
    public JSONObject recoverySort(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        return sortService.recoverySort(requestJson);
    }

    @RequestMapping("/upload")
    public Map imgUpload(HttpServletRequest request, MultipartHttpServletRequest multiReq) throws IOException {
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
        string = srcUrl;
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
}
