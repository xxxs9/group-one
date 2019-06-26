package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.PostService;
import com.heeexy.example.service.impl.PostServiceImpl;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

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

    @GetMapping("/list")
    public JSONObject listUser(HttpServletRequest request) {
        return postService.listPost(CommonUtil.request2Json(request));
    }

    /**
     * 修改点赞数量
     * @param
     * @return
     */
    @PostMapping("/updateLikeOffset")
    public JSONObject updateLikeOffset(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "likeOffset,postId");
        return postService.updateLikeOffset(requestJson);
    }

    @PostMapping("/updateBrowseOffset")
    public JSONObject updateBrowseOffset(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "browseOffset,postId");
        return postService.updateBrowseOffset(requestJson);
    }

    @PostMapping("/updatePostState")
    public JSONObject updatePostState(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "postState,postId");
        return postService.updatePostState(requestJson);
    }

    @PostMapping("/queryPostById")
    public JSONObject queryPostById(HttpServletRequest request) {
        JSONObject requestJson = CommonUtil.request2Json(request);
        CommonUtil.hasAllRequired(requestJson, "");
        return postService.queryPostById(requestJson);
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
        localFile.createNewFile();
        file.transferTo(localFile);
        map.put("code", 0);
        map.put("msg", "上传成功");
        map.put("desFilePath", desFilePath);
        map.put("url", srcUrl);
        return map;
    }

    @PostMapping("/updatePost")
    public JSONObject updatePost(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "postId");
        return postService.updatePost(requestJson);
    }


    @PostMapping("/deletePostImg")
    public JSONObject deletePostImg (@RequestBody JSONObject jsonObject) {
        File file = new File(jsonObject.getString("desFilePath"));
        file.delete();
        return CommonUtil.successJson();
    }

}
