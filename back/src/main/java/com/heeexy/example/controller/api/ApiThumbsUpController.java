package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.ThumbsUpService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: linchen
 * @description:
 * @date: 2019/7/1
 * @Version:
 */
@RestController
    @RequestMapping("/api/like")
public class ApiThumbsUpController {

    @Autowired
    private ThumbsUpService thumbsUpService;
    /**
     * create by: lc
     * description:
     * create time: 2019/7/9 18:09
     *
      * @param requestJson
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     */
    @PostMapping("/wholikeme")
    public List<JSONObject> getThumbsUp(@RequestBody JSONObject requestJson) {
//        CommonUtil.hasAllRequired(requestJson, "postId,userId");
        Integer userid = requestJson.getInteger("userid");
        requestJson.put("userId",userid);
        return thumbsUpService.getThumbsUp(requestJson);
    }
    @PostMapping("/mylike")
    public JSONObject getMyThumbsUp(@RequestBody JSONObject requestJson) {
//        CommonUtil.hasAllRequired(requestJson, "postId,userId");
        Integer userid = requestJson.getInteger("userid");
        requestJson.put("userId",userid);

        return thumbsUpService.getMyThumbsUp(requestJson);
    }
    @PostMapping("/addpostlike")
    public JSONObject updateThumbsUp(@RequestBody JSONObject requestJson) {
        Integer userid = requestJson.getInteger("userid");
        Integer tid = requestJson.getInteger("tid");
        requestJson.put("userId",userid);
        requestJson.put("postId",tid);
//        CommonUtil.hasAllRequired(requestJson, "postId,userId");
        return thumbsUpService.updateThumbsUp(requestJson);
    }
}
