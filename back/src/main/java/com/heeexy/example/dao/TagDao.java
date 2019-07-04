package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.heeexy.example.util.model.Tag;

import java.util.List;

public interface TagDao {
    /**
     * 新增标签
     */
    int addTag(JSONObject jsonObject);

    /**
     * 批量新增标签
     */
    int addListTag(Tag tag);

    /**
     * 查询标签数
     */
    int countTag(JSONObject jsonObject);
    /**
     * 根据标签名查询标签
     */
    int countTagByName(JSONObject jsonObject);

    /**
     * 标签列表
     */
    List<JSONObject> listTag(JSONObject jsonObject);

    /**
     * 所有标签列表
     */
    List<JSONObject> listAllTag(JSONObject jsonObject);

    /**
     * 更新标签
     */
    int updateTag(JSONObject jsonObject);

    /**
     * 修改标签状态
     */
    int updateByStatus(JSONObject jsonObject);

    /**
     * 根据标签名查找标签ID
     * @param jsonObject 标签名
     * @return 标签ID
     */
    JSONObject getTagByName(JSONObject jsonObject);

    /**
     * 删除所有标签
     */
    int deleteAllTag();
    /**
     * 根据父标签ID查找标签
     */
    int getTagByParentId(JSONObject jsonObject);

    /**
     *根据标签名查标签id
     * @param jsonObject
     * @return List<JSONObject>
     */
    List<JSONObject> getTagTextByTagId (JSONObject jsonObject);
}
