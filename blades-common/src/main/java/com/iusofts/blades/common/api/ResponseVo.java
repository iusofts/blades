package com.iusofts.blades.common.api;


public class ResponseVo<T> {

	private T data;

	/**
	 * 返回消息
	 */
	private String msg;

	/**
	 * 代码
	 */
	private Integer code;

	/**
	 * 是否成功
	 */
	private boolean success;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}