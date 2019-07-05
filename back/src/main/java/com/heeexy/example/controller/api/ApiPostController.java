package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.heeexy.example.service.impl.PostServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import static com.heeexy.example.controller.PostController.*;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/28 9:48
 * @description：小程序的接口
 * @version: 0.1$
 */
@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    @Autowired
    PostServiceImpl postService;

    /**
     * 获取帖子详情
     * @param requestJson 帖子ID 当前用户ID
     * @return
     */
    @PostMapping("/postDetail")
    public JSONObject postDetail(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "tid,userId");
        return postService.getDetailById(requestJson);
    }

    /**
     * 获取首页板块列表
     * @param requestJson 板块ID 当前用户ID
     * @return
     */
    @PostMapping("/stickPost")
    public JSONObject getStickPost(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "stickId,userId");
        return postService.getStickPost(requestJson);
    }

    @PostMapping("/typePost")
    public JSONObject getTypePost(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "typeId,userId");
        return postService.getTypePost(requestJson);
    }

    @PostMapping("/release")
    public JSONObject release(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "userId");
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/photoupload")
    public String myphotoupload(HttpServletRequest request) {
        JSONObject ret = new JSONObject();
        String key = "";
        String fileName = "";
        String fileNames = "";
        ret.put("success", false);
        ret.put("msg", "请求失败[PU01]");
        try {
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            Iterator<String> iterator = req.getFileNames();
            while (iterator.hasNext()) {
                MultipartFile file = req.getFile(iterator.next());
                fileName = file.getOriginalFilename();
                String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                fileNames = UUID.randomUUID() + String.valueOf(new Date().getTime()) + "." + prefix;
                InputStream input = file.getInputStream();
                // 创建OSSClient实例
                OSSClient ossClient = new OSSClient(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);
                // 上传文件流
                ossClient.putObject(BUCKETNAME, KEY + fileNames, input);
                ossClient.shutdown();
            }
            ret.put("success", true);
            ret.put("msg", key + fileNames);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "http://group-one.oss-cn-shenzhen.aliyuncs.com/images/" + key + fileNames;
    }
}
