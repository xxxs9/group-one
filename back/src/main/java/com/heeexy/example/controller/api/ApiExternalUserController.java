package com.heeexy.example.controller.api;


import com.alibaba.fastjson.JSONObject;

import com.heeexy.example.dao.ExternalUserDao;
import com.heeexy.example.service.ExternalUserService;
import com.heeexy.example.service.PostService;
import com.heeexy.example.util.AesCbcUtil;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: liminhao
 * @date: 2019-06-28 10:16
 * @description: 前台用户接口
 * @version:
 */
@RestController
@RequestMapping("/api/my")
public class ApiExternalUserController {

    @Autowired
    ExternalUserService externalUserService;

    /**
     * 用户（游客）登录
     * @param requestJson
     * @return
     */
    @PostMapping("/login")
    public JSONObject UserLogin(@RequestBody JSONObject requestJson,HttpServletRequest request){
        JSONObject user = externalUserService.userLogin(requestJson);
        HttpSession session = request.getSession();
        session.setAttribute("userId",user.getInteger("userId"));
        session.setAttribute("uuId",user.getInteger("userId"));
        return CommonUtil.successJson(user);
    }

    /**
     * 获取用户信息(发帖数量，关注数量，粉丝数量，点赞数量，收藏数量，头像、昵称)
     * @param requestJson key:userId
     * @return JSONObject
     */
    @PostMapping("/myself")
    public JSONObject getMyself(@RequestBody JSONObject requestJson){
//        CommonUtil.hasAllRequired(requestJson, "uuId");
        requestJson.put("uuId",requestJson.getInteger("userId"));
        return externalUserService.getMyself(requestJson);
    }

    /**
     * 获取关注的用户的信息
     * @param requestJson key:userId
     * @return JSONObject
     */
    @PostMapping("/others")
    public JSONObject getOthers(@RequestBody JSONObject requestJson){
//        CommonUtil.hasAllRequired(requestJson, "uuId");oe
//        requestJson.put("uuId",requestJson.getInteger("userId"));
        return externalUserService.getOthers(requestJson);
    }

    /**
     * 获取当前用户发布的帖子信息
     * @param requestJson key:userId
     * @return JSONObject
     */
    @PostMapping("/myposts")
    public JSONObject getMyPosts(@RequestBody JSONObject requestJson){
        requestJson.put("uuId",requestJson.getInteger("userId"));
        return externalUserService.getMyPost(requestJson);
    }

    /**
     * 获取该用户点赞过的帖子
     * @param requestJson key:userId
     * @return JSONObject
     */
    @PostMapping("/mylikes")
    public JSONObject getMyLikes(@RequestBody JSONObject requestJson){
        requestJson.put("uuId",requestJson.getInteger("userId"));
        return externalUserService.getMyLikePost(requestJson);
    }

    /**
     * 获取该用户的浏览记录
     * @param requestJson key:userId
     * @return JSONObject
     */
    @PostMapping("/myrecords")
    public JSONObject getMyRecords(@RequestBody JSONObject requestJson){
        requestJson.put("uuId",requestJson.getInteger("userId"));
        return externalUserService.getMyRecords(requestJson);
    }

    /**
     * 发布按钮
     * @param requestJson key:userId
     * @param request
     * @return
     */
    @PostMapping("/release")
    public JSONObject isHasPost(@RequestBody JSONObject requestJson,HttpServletRequest request){
        requestJson.put("userId",request.getSession().getAttribute("userId"));
        return externalUserService.releaseButton(requestJson);
    }


    /**
     * @Title: decodeUserInfo
     * @author：lizheng
     * @date：2018年3月25日
     * @Description: 解密用户敏感数据
     * @param encryptedData 明文,加密数据
     * @param iv   加密算法的初始向量
     * @param code  用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，使用code 换取 session_key api，将 code 换成 openid 和 session_key
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/decodeUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map decodeUserInfo(String encryptedData, String iv, String code) {

        Map map = new HashMap();

        // 登录凭证不能为空
        if (code == null || code.length() == 0) {
            map.put("status", 0);
            map.put("msg", "code 不能为空");
            return map;
        }

        // 小程序唯一标识 (在微信小程序管理后台获取)
        String wxspAppid = "wx18385lalalala";
        // 小程序的 app secret (在微信小程序管理后台获取)
        String wxspSecret = "bef47459d81a6eflalalalala";
        // 授权（必填）
        String grant_type = "authorization_code";

        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
        //////////////// ////////////////
        // 请求参数
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type="
                + grant_type;
        // 发送请求
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        // 解析相应内容（转换成json对象）
        net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(sr);

        // 获取会话密钥（session_key）
        String session_key = json.get("session_key").toString();
        // 用户的唯一标识（openid）
        String openid = (String) json.get("openid");

        //////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
        try {
            String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                map.put("status", 1);
                map.put("msg", "解密成功");

                net.sf.json.JSONObject userInfoJSON = net.sf.json.JSONObject.fromObject(result);
                Map userInfo = new HashMap();
                userInfo.put("openId", userInfoJSON.get("openId"));
                userInfo.put("nickName", userInfoJSON.get("nickName"));
                userInfo.put("gender", userInfoJSON.get("gender"));
                userInfo.put("city", userInfoJSON.get("city"));
                userInfo.put("province", userInfoJSON.get("province"));
                userInfo.put("country", userInfoJSON.get("country"));
                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
                // 解密unionId & openId;

                userInfo.put("unionId", userInfoJSON.get("unionId"));
                map.put("userInfo", userInfo);
            } else {
                map.put("status", 0);
                map.put("msg", "解密失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }



}

