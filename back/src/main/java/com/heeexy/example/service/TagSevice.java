package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface TagSevice {

    /**
     * 分页显示所有标签
     */
    JSONObject listTag(JSONObject jsonObject);
    /**
     * 显示所有标签
     */
    JSONObject listAllTag(JSONObject jsonObject);
    JSONObject getAllTags(JSONObject jsonObject);


    /**
     * 修改标签
     */
    JSONObject updateTag(JSONObject jsonObject);
    /**
     * 删除标签
     */
    JSONObject deleteTag(JSONObject jsonObject);
    /**
     * 增量导入
     */
    
    JSONObject batchImport(String fileName, MultipartFile file) throws Exception;
    /**
     * 覆盖导入
     */
    JSONObject coverImport(String fileName, MultipartFile file) throws Exception;


}
