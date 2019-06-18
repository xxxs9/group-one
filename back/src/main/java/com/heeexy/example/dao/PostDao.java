package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/17 23:43
 * @description：帖子Dao层
 * @version: 1.0$
 */
public interface PostDao {

    int countUser(JSONObject jsonObject);

    List<JSONObject> listPost(JSONObject jsonObject);
}
