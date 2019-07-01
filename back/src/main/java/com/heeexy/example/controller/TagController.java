package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.TagSevice;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
        /**
         * create by: lc
         * description: 分页显示标签
         * create time: 2019/6/29 10:27
         *
          * @param request
         * @return com.alibaba.fastjson.JSONObject
         */
        return tagSevice.listTag(CommonUtil.request2Json(request));
    }

    /**
     *
     * @param request
     * @return
     */
    @GetMapping("/listAllTag")
    public JSONObject listAllTag(HttpServletRequest request) {
        /**
         * create by: lc
         * description: 显示所有标签
         * create time: 2019/6/29 10:26
         *
          * @param request
         * @return com.alibaba.fastjson.JSONObject
         */
        return tagSevice.listAllTag(CommonUtil.request2Json(request));
    }
    @PostMapping("/deleteTag")
    public JSONObject deleteSort(@RequestBody JSONObject requestJson) {
        /**
         * create by: lc
         * description: 删除标签
         * create time: 2019/6/29 10:26
         *
          * @param requestJson
         * @return com.alibaba.fastjson.JSONObject
         */
        CommonUtil.hasAllRequired(requestJson, "id");
        return tagSevice.deleteTag(requestJson);
    }
    @PostMapping("/updateTag")
    public JSONObject updateSort(@RequestBody JSONObject requestJson) {
        /**
         * create by: lc
         * description: 修改标签
         * create time: 2019/6/29 10:25
         *
          * @param requestJson
         * @return com.alibaba.fastjson.JSONObject
         */
        CommonUtil.hasAllRequired(requestJson, "id,name");
        return tagSevice.updateTag(requestJson);
    }
    @PostMapping("/importTag")
    public JSONObject exImport(@RequestParam(value = "filename") MultipartFile file) throws Exception{
        /**
         * create by: lc
         * description: 增量导入
         * create time: 2019/6/29 10:25
         *
          * @param file
         * @return com.alibaba.fastjson.JSONObject
         */
        String fileName = file.getOriginalFilename();
        return tagSevice.batchImport(fileName, file);
    }
    @PostMapping("/importCoverTag")
    public JSONObject exImportCover(@RequestParam(value = "filename") MultipartFile file) throws Exception{
        /**
         * create by: lc
         * description: 覆盖导入
         * create time: 2019/6/29 10:26
         *
          * @param file
         * @return com.alibaba.fastjson.JSONObject
         */
        String fileName = file.getOriginalFilename();
        return tagSevice.coverImport(fileName, file);
    }

}
