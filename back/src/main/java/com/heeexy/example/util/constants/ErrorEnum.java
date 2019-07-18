package com.heeexy.example.util.constants;

/**
 * @author: hxy
 * @date: 2017/10/24 10:16
 */
public enum ErrorEnum {
	/*
	 * 错误信息
	 * */
	E_400("400", "请求处理异常，请稍后再试"),
	E_500("500", "请求方式有误,请检查 GET/POST"),
	E_501("501", "请求路径不存在"),
	E_502("502", "权限不足"),
	E_503("503", "请求参数格式有误，请检查参数"),
	E_10008("10008", "角色删除失败,尚有用户属于此角色"),
	E_10009("10009", "账户已存在"),
	E_10010("10010", "该帖子已收藏"),
	E_10011("10011", "已超过最大收藏数"),
	E_10012("10012","名字已存在"),
	E_10013("10013","上传文件格式不正确"),
	E_10014("10014","父标签名称请设为文本格式"),
	E_10015("10015","子标签名称请设为文本格式"),
	E_10016("10016","父标签名称未填写"),
	E_10017("10017","子标签名称未填写"),
	E_10018("10018","您没有这权限，请授权之后再操作"),
	E_10019("10019","字数超过限制"),
	E_20011("20011", "登陆已过期,请重新登陆"),
	E_20012("20012", "发布失败"),
	E_10020("10020","您没有该权限"),
	E_10021("10021","该模板不能被删除"),
	E_90003("90003", "缺少必填参数"),
	E_90004("90004", "请勿重复添加标签");



	private String errorCode;

	private String errorMsg;

	ErrorEnum(String errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

}