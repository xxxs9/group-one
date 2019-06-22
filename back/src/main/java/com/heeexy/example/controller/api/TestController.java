package com.heeexy.example.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.UserService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: chens
 * @description: test - api - out - shiro
 * @date: 2017/11/2 10:19
 */
@RestController
@RequestMapping("/api/user")
public class TestController {
	@Autowired
	private UserService userService;

	/**
	 * 查询用户列表
	 */
	@GetMapping("/list")
	public JSONObject listUser(HttpServletRequest request) {
		return userService.listUser(CommonUtil.request2Json(request));
	}

}
