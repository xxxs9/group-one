package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.heeexy.example.service.impl.PostServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static sun.security.x509.CertificateX509Key.KEY;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/18 10:53
 * @description：帖子的Controller层
 * @version: 1.0$
 */

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/post")
public class PostController {


    @Autowired
    private PostServiceImpl postService;

    public static String ENDPOINT = "http://oss-cn-shenzhen.aliyuncs.com";
    public static String ACCESSKEYID = "LTAIwBZ9y0vYYX7f";
    public static String ACCESSKEYSECRET = "ch00fGXs3NoO7fdcPpZ7Ko60fPp4eg";
    public static String BUCKETNAME = "group-one";
    public static String KEY = "images/";
    /**
     * 获取帖子列表
     * @param request 无参或者搜索数据
     * @return 返回帖子列表
     */
    @RequiresPermissions("post:list")
    @GetMapping("/list")
    public JSONObject listUser(HttpServletRequest request) {
        return postService.listPost(CommonUtil.request2Json(request));
    }

    /**
     * 修改点赞数量
     * @param requestJson 帖子ID，修改数量
     * @return
     */
    @RequiresPermissions("post:update")
    @PostMapping("/updateLikeOffset")
    public JSONObject updateLikeOffset(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "likeOffset,postId");
        return postService.updateLikeOffset(requestJson);
    }

    /**
     * 修改浏览量
     * @param requestJson 帖子ID，修改数量
     * @return
     */
    @RequiresPermissions("post:update")
    @PostMapping("/updateBrowseOffset")
    public JSONObject updateBrowseOffset(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "browseOffset,postId");
        return postService.updateBrowseOffset(requestJson);
    }

    /**
     * 更改帖子状态
     * @param requestJson 帖子ID，状态
     * @return
     */
    @RequiresPermissions("post:update")
    @PostMapping("/updatePostState")
    public JSONObject updatePostState(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "postState,postId");
        return postService.updatePostState(requestJson);
    }

    /**
     * 查询单个帖子信息
     * @param request 帖子ID
     * @return 单个帖子详情
     */
    @PostMapping("/queryPostById")
    public JSONObject queryPostById(HttpServletRequest request) {
        JSONObject requestJson = CommonUtil.request2Json(request);
        CommonUtil.hasAllRequired(requestJson, "postId");
        return postService.queryPostById(requestJson);
    }

    /**
     * 上传接口
     * @param req 上传请求
     * @param multiReq 上传的文件
     * @return
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
        map.put("code", 0);
        map.put("msg", "上传成功");
        map.put("desFilePath", desFilePath);
        map.put("url", srcUrl);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/photoupload")
    public JSONObject myphotoupload(HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
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
                String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
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
        ret.put("url","http://group-one.oss-cn-shenzhen.aliyuncs.com/images/" + key + fileNames);
        return ret;
    }

    /**
     * 修改帖子接口
     * @param requestJson 帖子ID，修改的内容（帖子内容、类型、电话、地址）
     * @return
     */
    @RequiresPermissions("post:update")
    @PostMapping("/updatePost")
    public JSONObject updatePost(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "postId");
        return postService.updatePost(requestJson);
    }


    /**
     * 删除上传成功的图片接口
     * @param jsonObject 图片地址
     * @return
     */
    @RequiresPermissions("post:delete")
    @PostMapping("/deletePostImg")
    public JSONObject deletePostImg (@RequestBody JSONObject jsonObject) {
        File file = new File(jsonObject.getString("desFilePath"));
        file.delete();
        return CommonUtil.successJson();
    }

    /**
     * 删除帖子标签接口
     * @param requestJson 帖子ID，标签名
     * @return
     */
    @RequiresPermissions("post:delete")
    @PostMapping("/deletePostTag")
    public JSONObject deletePostTag (@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "postId,tagName");
        return postService.deletePostTag(requestJson);
    }

    /**
     * 添加帖子标签接口
     * @param requestJson 帖子ID，标签ID数组
     * @return
     */
    @RequiresPermissions("post:add")
    @PostMapping("/addPostTag")
    public JSONObject addPostTag(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "postId,addPostTagId");
        return postService.addPostTag(requestJson);
    }


}
