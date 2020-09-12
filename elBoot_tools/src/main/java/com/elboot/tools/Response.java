package com.elboot.tools;


import com.elboot.enums.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@SuppressWarnings("serial")
public class Response extends HashMap<String,Object> {

	private final static String CODE = "code";
	private final static String DATA = "data";
	private final static String TOTAL = "total";
	private final static String MESSAGE = "message";


	private Logger logger = LoggerFactory.getLogger(Response.class);

	private Response() {
		this.put(CODE, ErrorEnum.SUCCESS.getCode());
        this.put(MESSAGE, ErrorEnum.SUCCESS.getMessage());
	}



    //静态方法------------------------------------------------------------------------------
	public static Response newResponse() {
		return new Response();
	}

    public static Response set(Integer total, Object rows) {
        Response response = new Response();
        response.setTotal(total);
        response.ok(rows);

        return response;
    }

    public static Response set(Object rows) {
        Response response = new Response();
        response.ok(rows);

        return response;
    }

    public static Response set(String key, Object value) {
        Response response = Response.newResponse();
        response.put(key, value);
        return response;
    }






	/**
	 * @param data
	 */
	public Response setData(Object data){
		this.put(DATA, data);
		return this;
	}

	public Response put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	/**
	 * 获得错误消息
	 */
	public String getMessage() {
		Object msg = this.get(MESSAGE);
		return msg != null ? msg.toString() : null;
	}

	public Response moveTo(String fromKey, String toKey) {
		Object val = this.get(fromKey);
		this.put(toKey, val);
		this.remove(fromKey);
		return this;
	}

	public Boolean isOK() {
		return this.getCode() == ErrorEnum.SUCCESS.getCode();
	}

	public Boolean isFail() {
		return !isOK();
	}

	public Response ok(Object data) {
		super.put(DATA, data);
		return this;
	}

	public Response ok(String key, Object val) {
		super.put(key, val);
		return this;
	}

	public Response setResults(Integer count, Object data) {
		this.setTotal(count);
		this.ok(data);
		return this;
	}

	private Response setCode(int code) {
		this.put(CODE, code);
		return this;
	}

	/**
	 * 设置返回码与返回信息
	 *
	 * 强列建议在ErrorEnum 中定义错误, 再调用setError()方法
	 *
	 * @param code
	 * @param message
	 * @return
	 */
	public Response setCodeAndMessage(int code, String message) {
		this.put(CODE, code);
		this.put(MESSAGE, message);
		return this;
	}


    /**
     * 设置返回信息
     * 此方法只能用于标明一些已有错误code,但需要特别定制的返回消息时用到
     * 请不要滥用此方法
     *
     * @param message
     * @return
     */
    public Response setMessage(String message) {
        this.put(MESSAGE, message);
        return this;
    }

	public int getCode() {
		return Integer.parseInt(this.get(CODE).toString());
	}


	public Response setTotal(Integer total) {
		this.put(TOTAL, total);
		return this;
	}

	public Response setError(ErrorEnum errorEnum){
		this.setCode(errorEnum.getCode());
		this.setMessage(errorEnum.getMessage());
		return this;
	}


//============状态===================================================================================

    public Response OK() {
		this.setCode(ErrorEnum.SUCCESS.getCode());
		this.put(MESSAGE, "ok");
		return this;
	}



}
