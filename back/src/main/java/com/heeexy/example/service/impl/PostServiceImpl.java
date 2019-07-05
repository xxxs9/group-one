package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.*;
import com.heeexy.example.service.PostService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.heeexy.example.util.constants.ErrorEnum.E_20012;
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

    @Autowired
    private SortDao sortDao;

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


    /*****************************************************小程序数据*************************************************************/

    /**
     * 获取帖子详情
     * @param jsonObject 帖子ID (tid)，当前用户ID
     * @return 帖子详情
     */
    @Override
    public JSONObject getDetailById(JSONObject jsonObject) {
        jsonObject.put("postId",jsonObject.get("tid"));
        ArrayList<JSONObject> list = new ArrayList<>();
        list.add(jsonObject);
        List<JSONObject> userId = getPostList(list, jsonObject.get("userId"));
        JSONObject postDetail = new JSONObject();
        for (JSONObject object : userId) {
            postDetail = object;
        }
        List<JSONObject> likeUserId = thumbsUpDao.getLikeList(postDetail);
        ArrayList<JSONObject> likeList = new ArrayList<>();
        likeUserId.size();
        for (JSONObject object : likeUserId) {
            object.put("uuId",object.get("userId"));
            JSONObject iconById = externalUserDao.findIconById(object);
            iconById.size();
            JSONObject likeUser = new JSONObject();
            likeUser.put("likeid",object.get("userId"));
            likeUser.put("likeimg",iconById.get("iconUrl"));
            likeUser.put("likename",iconById.get("username"));
            likeList.add(likeUser);
        }
        postDetail.put("likelist",likeList);
        return CommonUtil.successJson(postDetail);
    }


    /**
     * 获取帖子列表（非详情）
     * @param jsonObject postIdList[postId](帖子ID数组),userId(当前用户ID)
     * @return
     */
    @Override
    public List<JSONObject> getPostListApi(JSONObject jsonObject) {
        JSONArray postIdList = jsonObject.getJSONArray("postIdList");
        //判断帖子列表是否为空
        if (postIdList.size() == 0){
            return null;
        }
        Integer userId = jsonObject.getInteger("userId");
        //判断当前用户ID是否为空
        if(userId == 0){
            return null;
        }
        List<JSONObject> postList = postDao.getPostListApi(jsonObject);
        //循环列表内容（处理单个帖子内容）
        for (JSONObject object : postList) {
            object.put("userId",jsonObject.get("userId"));
            object.put("uuId",object.get("uid"));
            JSONObject postOwner = externalUserDao.findIconById(object);/*获取发帖用户ID和头像*/
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
            int likestate = thumbsUpDao.queryExist(object);
            if(likestate > 0){
                object.put("likestate",true);
            }else {
                object.put("likestate",false);
            }
            List<String> likeUserList = new ArrayList<>();
            //获取点赞的用户IDList
            List<JSONObject> likeListId = thumbsUpDao.getLikeList(object);
            int likeCount = thumbsUpDao.countLikes(jsonObject) + object.getInteger("likeOffset");
            //点赞用户列表循环
            likeListId.size();
            for (JSONObject likeUser : likeListId) {
                likeUser.put("uuId",likeUser.get("userId"));
                //查询点赞用户的昵称
                JSONObject iconById = externalUserDao.findIconById(likeUser);
                if(iconById == null || iconById.size() == 0){
                    continue;
                }
                String username = iconById.getString("username");
                if(username != null){
                    likeUserList.add(username);
                }
            }
            if(likeCount > 6 && likeUserList.size() < 6){
                //点赞用户不足6个时补齐6个
                for(int i = likeUserList.size();i < 6 ;i++){
                    likeUserList.add("游客");
                }
            }
            likeListId.clear();
            object.put("good",likeUserList);
            //整合最低价和最高价
            Integer priceFloor = object.getInteger("priceFloor");
            Integer priceTop = object.getInteger("priceTop");
            String price = "";
            if(priceTop == 0){
                price = "参考价格：" + priceFloor;
            }else {
                price = "参考价格：" + priceFloor + "---" + priceTop;
            }
            object.put("desc",object.getString("desc") + "\n" + price);
            Date postTime = object.getDate("postTime");
            //计算时间距离日期
            object.put("time",StringTools.differentDaysByMillisecond(postTime));
            //获取评论，整理评论内容
            List<JSONObject> comments = (List<JSONObject>) object.get("comments");
            for (JSONObject comment : comments) {
                commentText(comment);
            }
            object.remove("priceFloor");
            object.remove("priceTop");
            object.remove("userId");
            object.remove("uuId");
            object.remove("postTime");
            object.remove("likeOffset");
        }
        return postList;
    }

    /**
     * 整合评论内容
     * @param comment 单个评论对象
     * @return
     */
    private void commentText(JSONObject comment){
        String comusername = "";
        String acceptUserName = comment.getString("acceptUserName");
        String commentUserName = comment.getString("commentUserName");
        if(acceptUserName == null || "".equals(acceptUserName)){
            comusername = commentUserName;
        }else {
            comusername = commentUserName + ": " + "@" + acceptUserName;
        }
        comment.put("commentUserName",comusername);
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
        Integer stickId = jsonObject.getInteger("stickId");
        List<JSONObject> postList = new ArrayList<>();
        if(stickId == 5){
            ArrayList<JSONObject> tagPostList = new ArrayList<>();
            List<JSONObject> month = postDao.getPostByTypeId(sortDao.getIdByName("月租"));
            List<JSONObject> shorth = postDao.getPostByTypeId(sortDao.getIdByName("短租"));
            tagPostList.addAll(month);
            tagPostList.addAll(shorth);
            postList = getPostList(tagPostList, userId);
        }else {
            //获取置顶帖子
            List<JSONObject> postById = stickDao.getPostById(jsonObject);
            List<JSONObject> topPostList = new ArrayList<>();
            topPostList = getPostList(postById, userId);
            if(null == topPostList || topPostList.size() ==0){

            }else {
                for (JSONObject object : topPostList) {
                    String postStick = object.getString("postStick");
                    if(!postStick.equals(stickId) && !"1".equals(postStick)){
                        topPostList.remove(object);
                    }
                    object.put("top",true);
                }
                postList.addAll(topPostList);
            }
            //获取剩余帖子
            List<JSONObject> postIdByStick = postDao.getPostIdByStick(jsonObject);
            List<JSONObject> otherPostList = getPostList(postIdByStick, userId);
            List<JSONObject> endPost = new ArrayList<>();
            if(stickId == 4){
                otherPostList.sort(new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        return ((Integer) o2.get("seepeople")).compareTo((Integer) o1.get("seepeople"));
                    }
                });
                Iterator<JSONObject> iter = otherPostList.iterator();
                while(iter.hasNext()) {
                    JSONObject object = iter.next();
                    String time = object.getString("time");
                    if ("15天前".equals(time)) {
                        endPost.add(object);
                        iter.remove();
                    }
                }
            }
            postList.addAll(otherPostList);
            if(endPost.size() != 0){
                postList.addAll(endPost);
            }
        }
        jsonObject.put("postList",postList);
        return jsonObject;
    }

    /**
     *
     * @param
     * @return
     */
    private List<JSONObject> getPostList(List<JSONObject> postIdList, Object userId){
        JSONObject stickObj = new JSONObject();
        stickObj.put("postIdList",postIdList);
        stickObj.put("userId",userId);
        return getPostListApi(stickObj);
    }

    /**
     * 获取对应类型的帖子
     * @param jsonObject 类型ID
     * @return
     */
    @Override
    public JSONObject getTypePost(JSONObject jsonObject) {
        List<JSONObject> typeId = postDao.getPostByTypeId(jsonObject.getInteger("typeId"));
        jsonObject.put("typePost",getPostList(typeId,jsonObject.get("userId")));
        return jsonObject;
    }

    /**
     * 发布
     * @param jsonObject 发布的内容
     * @return
     */
    @Override
    public JSONObject release(JSONObject jsonObject) {
        boolean verify = verify(jsonObject);
        if (!verify) {
            return CommonUtil.errorJson(E_20012);
        }


        return CommonUtil.successJson();

    }

    private boolean verify(JSONObject jsonObject){
        JSONObject releaseData = new JSONObject();
        boolean flag = true;
        //帖子正文
        String content = jsonObject.getString("content");
        if(content.length()>=200){
            flag = false;
        }
        //帖子类型ID
        Integer typeId = sortDao.getIdByName(jsonObject.getString("typeId"));
        if(!typeId.toString().matches("/^\\d+/")){
            flag = false;
        }
        //帖子地址
        String address = jsonObject.getString("address");
        if(!address.matches("/[a-zA-Z0-9\\u0391-\\uFFE5]+$/")){
            flag = false;
        }
        //帖子电话
        String telephone = jsonObject.getString("telephone");
        if(!telephone.matches("/^1[3|4|5|8][0-9]\\d{8}$/")){
            flag = false;
        }
        //帖子发布时间
        Date nowDate = new Date();
        if(!nowDate.toString().matches(" /^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$/")){
            flag = false;
        }
        //月租短租输入时间
        Date startdata = jsonObject.getDate("startdata");//起始时间
        Date enddata = jsonObject.getDate("enddata");//结束时间
        if((startdata.toString()!=null && !startdata.toString().equals("")) && (enddata.toString()!=null && !enddata.toString().equals(""))){
            if(!startdata.toString().
                    matches("^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$") ||
                    !enddata.toString().
                            matches("^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$")){
                flag = false;
            }
        }
        //发帖用户ID
        String uuid = jsonObject.getString("userId");
        //图片数组
        JSONArray imglist = jsonObject.getJSONArray("imglist");


        return flag;
    }
}
