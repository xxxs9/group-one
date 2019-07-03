package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/17 23:43
 * @description：帖子Dao层
 * @version: 1.0$
 */
public interface PostDao {

    /**
     * 统计帖子总数
     * @param jsonObject 不需要
     * @return 总数
     */
    int countUser(JSONObject jsonObject);

    /**
     * 查询帖子列表
     * @param jsonObject 单列表查询不需要参数，根据内容查询需要关键字作为参数
     * @return 帖子列表
     */
    List<JSONObject> listPost(JSONObject jsonObject);

    /**
     * 根据帖子ID查询
     * @param jsonObject 帖子ID
     * @return 单个帖子详细
     */
    JSONObject queryPostById(JSONObject jsonObject);

    /**
     * 增加点赞数量
     * @param jsonObject 帖子ID,要增加点赞的数量
     * @return
     */
    int updateLikeOffset(JSONObject jsonObject);

    /**
     * 增加浏览数量
     * @param jsonObject 帖子ID,要增加的浏览量
     * @return
     */
    int updateBrowseOffset(JSONObject jsonObject);

    /**
     * 修改帖子状态
     * @create time: 2019/6/19 14:05
     * @param jsonObject 帖子ID 状态0，1
     * @return
     */
    int updatePostState(JSONObject jsonObject);

    /**
     * 修改帖子内容
     * @param jsonObject 帖子id 帖子内容
     * @return
     */
    int updatePost(JSONObject jsonObject);

    /**
     * 返回对应帖子的图片列表
     * @param jsonObject 帖子ID
     * @return
     */
    List<JSONObject> listPostImg(JSONObject jsonObject);

    /**
     * 根据图片ID删除图片
     * @param jsonObject 图片ID
     * @return
     */
    int deleteImgById(JSONObject jsonObject);

    /**
     * 添加对应帖子的图片
     * @param jsonObject 帖子ID 图片ID
     * @return
     */
    int addPostImg(JSONObject jsonObject);

    /**
     * 根据标签搜索帖子ID
     * @param jsonObject 标签ID
     * @return 帖子ID数组
     */
    List<JSONObject> listPostIdByTag(JSONObject jsonObject);

    /**
     * 删除帖子标签
     * @param jsonObject 帖子ID  标签ID
     * @return
     */
    int deletePostTag(JSONObject jsonObject);

    /**
     * 根据帖子ID查找标签
     * @param jsonObject 帖子ID
     * @return
     */
    List<JSONObject> listTagByPostId(JSONObject jsonObject);

    /**
     * 添加帖子标签
     * @param jsonObject 帖子ID 标签ID
     * @return
     */
    int addPostTag(JSONObject jsonObject);

    /**
     * 根据帖子ID和标签ID查找关联表
     * @param jsonObject 帖子ID ，标签ID
     * @return
     */
    JSONObject getTagById(JSONObject jsonObject);


    /********************************小程序数据*******************************************/
    
    /**
     * 获取前台帖子列表
     * @param jsonObject postIdList[]
     * @return 
     */
    List<JSONObject> getPostListApi(JSONObject jsonObject);

    /**
     * 获取板块的帖子ID
     * @param
     * @return
     */
    List<JSONObject> getPostIdByStick(JSONObject jsonObject);
}
