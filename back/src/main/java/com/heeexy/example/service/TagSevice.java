package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface TagSevice {

    JSONObject addTag(JSONObject jsonObject);

    /**
     * 文章列表
     */
    JSONObject listTag(JSONObject jsonObject);

    JSONObject listAllTag(JSONObject jsonObject);


    /**
     * 更新文章
     */
    JSONObject updateTag(JSONObject jsonObject);
    JSONObject deleteTag(JSONObject jsonObject);
    JSONObject batchImport(String fileName, MultipartFile file) throws Exception;
    JSONObject coverImport(String fileName, MultipartFile file) throws Exception;


}
