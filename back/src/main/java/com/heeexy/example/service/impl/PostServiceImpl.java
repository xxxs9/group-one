package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.CommentDao;
import com.heeexy.example.dao.ExternalUserDao;
import com.heeexy.example.dao.PostDao;
import com.heeexy.example.dao.TagDao;
import com.heeexy.example.service.PostService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.heeexy.example.util.constants.ErrorEnum.E_90003;
import static com.heeexy.example.util.constants.ErrorEnum.E_90004;

/**
 * @author ：Hooonheng
 * @date ：Created in 2019/6/18 10:47
 * @description：
 * @version: $
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDao postDao;

    @Autowired
    private ExternalUserDao externalUserDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private TagDao tagDao;

    /**
     * 帖子列表
     * @param jsonObject 页码
     * @return 返回帖子列表
     */
    @Override
    public JSONObject listPost(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        String queryOwnewName = jsonObject.getString("queryOwnewName");
        if(queryOwnewName != null && "".equals(queryOwnewName)==false){
            JSONObject queryKey = new JSONObject();
            queryKey.put("querykey",queryOwnewName);
            List<JSONObject> user = externalUserDao.getUser(queryKey);
            List<Integer> postOwnerIdList = new ArrayList<>();
            for (JSONObject object : user) {
                postOwnerIdList.add(object.getInteger("uuId"));
            }
            jsonObject.put("postOwnerId",postOwnerIdList);
        }
        String queryPostTagId = jsonObject.getString("queryPostTagId");
        if(null != queryPostTagId && !"".equals(queryPostTagId) && queryPostTagId.length()!=0){
            String[] split = queryPostTagId.split("-");
            List<String> tagIdList = new ArrayList<>();
            for (String s : split) {
                if("".equals(s)==false){
                    tagIdList.add(s);
                }
            }
            if(tagIdList.size()!=0 || tagIdList != null){
                jsonObject.put("queryPostTagId",tagIdList);
                List<JSONObject> postIdList = postDao.listPostIdByTag(jsonObject);
                if(postIdList.size()==0 || postIdList == null){
                    ArrayList<Object> list = new ArrayList<>();
                    return CommonUtil.successJson(list);
                }
                jsonObject.put("postIdList",postIdList);
            }
        }
        int count = postDao.countUser(jsonObject);
        List<JSONObject> list = postDao.listPost(jsonObject);
        for (JSONObject object : list) {
            object.put("likeCount",Integer.parseInt(object.getString("likeOffset")) + Integer.parseInt(object.getString("likeCount")));
            object.put("browseCount",Integer.parseInt(object.getString("browseOffset")) + Integer.parseInt(object.getString("browseCount")));
        }
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 增加点赞数量
     * @param jsonObject 帖子ID，点赞数量
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateLikeOffset(JSONObject jsonObject) {
        postDao.updateLikeOffset(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 增加浏览数量
     * @param jsonObject 帖子ID，浏览量
     * @return
     */
    @Override
    public JSONObject updateBrowseOffset(JSONObject jsonObject) {
        postDao.updateBrowseOffset(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 改变帖子状态
     * @param jsonObject 帖子ID，状态 ：0为显示 1为隐藏
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updatePostState(JSONObject jsonObject) {
        postDao.updatePostState(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 查找单个帖子详情
     * @param jsonObject 帖子ID
     * @return
     */
    @Override
    public JSONObject queryPostById(JSONObject jsonObject) {
        JSONObject DetailData = postDao.queryPostById(jsonObject);
        JSONObject postUser = externalUserDao.findUserById(DetailData.getInteger("postOwnerId"));
        List<JSONObject> comment = commentDao.getByPostId(jsonObject);
        DetailData.put("externalUser",postUser);
        DetailData.put("comments",comment);
        return CommonUtil.successJson(DetailData);
    }

    /**
     * 修改帖子内容
     * @param jsonObject 帖子id,修改的内容（帖子内容、类型、电话、地址）
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updatePost(JSONObject jsonObject) {
        List<JSONObject> imgSrcList = postDao.listPostImg(jsonObject);
        JSONArray deleteImgList = jsonObject.getJSONArray("deleteImgList");
        for (JSONObject oldImg : imgSrcList) {
            for (Object delImg : deleteImgList) {
                if(oldImg.getString("postImageSrc").equals(delImg)){
                    postDao.deleteImgById(oldImg);
                }
            }
        }
        JSONArray newImgList = jsonObject.getJSONArray("newImgList");
        if(null != newImgList || newImgList.size() !=0){
            for (Object src : newImgList) {
                JSONObject addImg = new JSONObject();
                addImg.put("postImageSrc",src);
                addImg.put("postId",jsonObject.getString("postId"));
                postDao.addPostImg(addImg);
            }
        }
        newImgList.clear();
        postDao.updatePost(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 删除帖子对应标签
     * @param jsonObject 帖子ID 标签ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject deletePostTag(JSONObject jsonObject) {
        jsonObject.put("tagId",tagDao.getTagByName(jsonObject));
        int i = postDao.deletePostTag(jsonObject);
        if(i>0){
            List<JSONObject> jsonObjects = postDao.listTagByPostId(jsonObject);
            List<Object> list = new ArrayList<>();
            for (JSONObject object : jsonObjects) {
                list.add(object.getString("tagName"));
            }
            return CommonUtil.successJson(list);
        }
        return CommonUtil.successJson();
    }

    /**
     * 添加帖子标签
     * @param jsonObject 帖子ID ，标签ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addPostTag(JSONObject jsonObject) {
        JSONArray addPostTag = jsonObject.getJSONArray("addPostTagId");
        if(addPostTag != null || addPostTag.size()!=0){
            for (Object o : addPostTag) {
                jsonObject.put("tagId",o);
                jsonObject.put("parentId",1);
                JSONObject tagById = postDao.getTagById(jsonObject);
                if(tagById == null){
                    postDao.addPostTag(jsonObject);
                }else {
                    return CommonUtil.errorJson(E_90004);
                }
            }
        }else {
            return CommonUtil.errorJson(E_90003);
        }
        List<JSONObject> jsonObjects = postDao.listTagByPostId(jsonObject);
        List<Object> list = new ArrayList<>();
        for (JSONObject object : jsonObjects) {
            list.add(object.getString("tagName"));
        }
        return CommonUtil.successJson(list);
    }


    /********************************************小程序数据****************************************************/

    /**
     * 获取帖子详情
     * @param jsonObject 帖子ID ，当前用户ID
     * @return 帖子详情
     */
    @Override
    public JSONObject getDetailById(JSONObject jsonObject) {
        JSONObject postDetail = queryPostById(jsonObject);


        return null;
    }
}
