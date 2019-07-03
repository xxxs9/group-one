package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.*;
import com.heeexy.example.service.PostService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.StringTools;
import com.heeexy.example.util.constants.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.heeexy.example.util.constants.ErrorEnum.E_90003;
import static com.heeexy.example.util.constants.ErrorEnum.E_90004;

/**
 * @author ：Hooon
 * @date ：Created in 2019/6/18 10:47
 * @description：
 * @version: $2.0
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

    @Autowired
    private CollectionDao collectionDao;

    @Autowired
    private BrowseRecordDao browseRecordDao;

    @Autowired
    private ThumbsUpDao thumbsUpDao;

    @Autowired
    private StickDao stickDao;

    /**
     * 帖子列表
     * @param jsonObject 页码
     * @return 返回帖子列表
     */
    @Override
    public JSONObject listPost(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        //发帖用户昵称查询
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
        //标签查询
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
    @Transactional(rollbackFor = Exception.class)
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
        //获取帖子详情
        JSONObject postDetail = postDao.queryPostById(jsonObject);
        JSONObject postOwner = externalUserDao.findUserById(postDetail.getInteger("postOwnerId"));
        int collect = collectionDao.getByPostIdCount(jsonObject);
        if(collect == 0){
            postDetail.put("collectionState",false);
        }else {
            postDetail.put("collectionState",true);
        }
        List<JSONObject> byPostId = commentDao.getByPostId(jsonObject);
        postDetail.put("comments",byPostId);
        postDetail.put("postOwnerName",postOwner.get("username"));
        postDetail.put("postOwnerUrl",postOwner.get("iconUrl"));
        postDetail.put("browseCount",browseRecordDao.countPostBrowse(jsonObject) + postDetail.getInteger("browseOffset"));
        postDetail.put("likeCount",thumbsUpDao.countLikes(jsonObject) + postDetail.getInteger("likeOffset"));
        postDetail.remove("browseOffset");
        postDetail.remove("likeOffset");
        return CommonUtil.successJson(postDetail);
    }


    /**
     * 获取帖子列表（非详情）
     * @param jsonObject postIdList[postIdo9](帖子ID数组),userId(当前用户ID)
     * @return
     */
    @Override
    public List<JSONObject> getPostListApi(JSONObject jsonObject) {
        //获取帖子ID列表
        JSONArray postIdList = jsonObject.getJSONArray("postIdList");
        //判断帖子列表是否为空
        if (postIdList == null && postIdList.size() ==0){
            return null;
        }
        //获取当前用户ID
        Integer userId = jsonObject.getInteger("userId");
        //判断当前用户ID是否为空
        if(userId == 0 && userId == null){
            return null;
        }
        //获取列表
        List<JSONObject> postList = postDao.getPostListApi(jsonObject);
        //循环列表内容（处理单个帖子内容）
        for (JSONObject object : postList) {
            object.put("userId",jsonObject.get("userId"));
            object.put("uuId",object.get("uid"));
            //获取发帖用户ID和头像
            JSONObject postOwner = externalUserDao.findIconById(object);
            object.put("username", postOwner.get("username"));
            object.put("avatarurl", postOwner.get("iconUrl"));
            //查询是否被收藏
            int collect = collectionDao.getByPostIdCount(object);
            //0为未被收藏，1为已收藏
            if(collect == 0){
                object.put("collectionState",false);
            }else {
                object.put("collectionState",true);
            }
            //计算浏览量（统计浏览量列表+偏移量）
            object.put("seepeople",browseRecordDao.countPostBrowse(object) + object.getInteger("seepeople"));
            object.put("comments",commentDao.getByPostId(object));
            //帖子ID
            object.put("tid",object.get("postId"));
            //点赞用户昵称的List
            List<String> likeUserList = new ArrayList<>();
            //获取点赞的用户IDList
            List<JSONObject> likeListId = thumbsUpDao.getLikeList(object);
            //点赞用户列表循环
            for (JSONObject likeUser : likeListId) {
                likeUser.put("uuId",likeUser.get("userId"));
                //查询点赞用户的昵称
                JSONObject iconById = externalUserDao.findIconById(likeUser);
                likeUserList.add(iconById.getString("username"));
            }
            //点赞用户不足6个时补齐6个
            if(likeUserList.size() < 6){
                for(int i = likeUserList.size();i < 6 ;i++){
                    likeUserList.add("游客");
                }
            }
            Integer priceFloor = object.getInteger("priceFloor");
            Integer priceTop = object.getInteger("priceTop");
            String price = "";
            if(priceTop == 0){
                price = "参考价格：" + priceFloor;
            }else {
                price = "参考价格：" + priceFloor + "---" + priceTop;
            }
            String desc = object.getString("desc");
            object.put("desc",desc + "\n" + price);
            object.put("good",likeUserList);
//            String postTime = object.getString("postTime");
//            String time = StringTools.CalculateTime(postTime);
//            object.put("time",time);

            object.remove("uuId");
        }
        return postList;
    }

    /**
     * 
     * @param jsonObject stickId 板块ID
     * @return 
     */
    @Override
    public JSONObject getStickPost(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        Object userId = jsonObject.get("userId");
        String stickId = jsonObject.getString("stickId");
        //获取置顶帖子
        List<JSONObject> postById = stickDao.getPostById(jsonObject);
        List<JSONObject> topPostList = getPostList(postById, userId);
        if(topPostList != null || topPostList.size()>0){
            for (JSONObject object : topPostList) {
                String postStick = object.getString("postStick");
                if(!postStick.equals(stickId) && !"1".equals(postStick)){
                    topPostList.remove(object);
                }
                object.put("top",true);
            }
        }

        //获取剩余帖子
        List<JSONObject> postIdByStick = postDao.getPostIdByStick(jsonObject);
        List<JSONObject> otherPostList = getPostList(postIdByStick, userId);
        for (JSONObject object : otherPostList) {
            object.put("top",false);
        }
        Collections.sort(otherPostList, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                return ((Integer)o2.get("browseCount")).compareTo((Integer)o1.get("browseCount"));
            }
        });
        topPostList.addAll(otherPostList);
        jsonObject.put("postList",topPostList);
        return jsonObject;
    }

    public List<JSONObject> getPostList(List<JSONObject> postIdList,Object userId){
        JSONObject stickObj = new JSONObject();
        stickObj.put("postIdList",postIdList);
        stickObj.put("userId",userId);
        List<JSONObject> postStickList = getPostListApi(stickObj);
        return postStickList;
    }
}
