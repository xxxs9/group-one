package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.TagSevice;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: linchen
 * @description:
 * @date: 2019/6/21
 * @Version:
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagSevice tagSevice;

    @GetMapping("/listTag")
    public JSONObject listTag(HttpServletRequest request) {
        return tagSevice.listTag(CommonUtil.request2Json(request));
    }
    @GetMapping("/listAllTag")
    public JSONObject listAllTag(HttpServletRequest request) {
        return tagSevice.listAllTag(CommonUtil.request2Json(request));
    }
    @PostMapping("/deleteTag")
    public JSONObject deleteSort(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        return tagSevice.deleteTag(requestJson);
    }
    @PostMapping("/updateTag")
    public JSONObject updateSort(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,name");
        return tagSevice.updateTag(requestJson);
    }
    @PostMapping("/importTag")
    public JSONObject exImport(@RequestParam(value = "filename") MultipartFile file) throws Exception{
        String fileName = file.getOriginalFilename();
        return tagSevice.batchImport(fileName, file);
    }
    @PostMapping("/importCoverTag")
    public JSONObject exImportCover(@RequestParam(value = "filename") MultipartFile file) throws Exception{
        String fileName = file.getOriginalFilename();
        return tagSevice.coverImport(fileName, file);
    }

}
