package com.elboot.tools;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @Author zhuyuc
 * @Date by 2020/8/21
 * @Email fangc@jsaepay.com
 * @Company:亚银网络科技有限公司
 * @Description:封装后的HashMap，继承自BaseMap，最常用的场景是在Controller中获取request的参数
 * 并传递到Service, Dao中供数据库所需参数使用
 * 注意：！！！！为了规范代码，此Map仅仅用于查询的时候传参使用，不可用于其它地方！！！！
 */
public class ParamMap extends BaseMap {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final String PAGE_INDEX = "pageIndex";
	private static final String PAGE_SIZE = "pageSize";
	private static final String OFFSET = "offset";
	private static final String LIMIT = "limit";
	private static final String START = "start";
	private static final String END = "end";
	private Boolean format = true;

	private ParamMap() {
	}

	public static ParamMap init(String key, Object value) {
		ParamMap param = new ParamMap(key, value);
		return param;
	}

	public static ParamMap init(HttpServletRequest request) {
		ParamMap param = ParamMap.newMap();
		param.setHttpMap(request.getParameterMap());
		return param;
	}

	public static ParamMap init(HttpServletRequest request, Boolean format) {
		ParamMap param = ParamMap.newMap();
		param.format = format;
		param.setHttpMap(request.getParameterMap());
		return param;
	}

	public static ParamMap init(Map<?,?> map) {
		ParamMap param = ParamMap.newMap();
		param.setMap(map);
		return param;
	}

	public static ParamMap init() {
		ParamMap param = new ParamMap();
		return param;
	}

	public ParamMap(Map<?,?> map) {
		this.setMap(map);
	}

	public static ParamMap newMap() {
		return ParamMap.init();
	}

	public ParamMap(HttpServletRequest request) {
		this.setMap(request.getParameterMap());
	}

	public ParamMap(String key, Object value){
		this.put(key, value);
	}

	public Integer getPageIndex() {
		return this.getInt(PAGE_INDEX);
	}

	public Integer getPageSize() {
		return this.getInt(PAGE_SIZE);
	}

	//从http requestMap中获取值
	private void setHttpMap(Map<?,?> map) {
		for(Object key : map.keySet()) {
			Object[] value = (Object[])map.get(key);

			String skey = key.toString();
			Object val = value[0];

			this.set(skey, val);
		}

		setPages();
		if(this.format) {
			setDateTimes();
		}
	}

	private void setMap(Map<?,?> map) {
        if(map != null)
		for(Object key : map.keySet()) {
			this.set(key.toString(), map.get(key));
		}
		setPages();
		if(this.format) {
			setDateTimes();
		}
	}

	//对日期时间调整到秒
	private void setDateTimes() {
		if(this.containsKey(START)) {
			this.setBeginDateTimeStr(START);
		}

		if(this.containsKey(END)) {
			this.setEndDateTimeStr(END);
		}
	}

	//计算分页所需参数
	public ParamMap setPages(int pageIndex, int pageSize) {
		this.put(PAGE_INDEX, pageIndex);
		this.put(PAGE_SIZE, pageSize);
		this.setPages();
        return this;
	}

	public ParamMap setPagesV2(int pageIndex, int pageSize, int reduce) {
		if (pageIndex == 0) throw new RuntimeException("索引必须大于0");
		this.put(PAGE_INDEX, pageIndex);
		this.put(PAGE_SIZE, pageSize);
		this.setPagesV2(reduce);
		return this;
	}

	//计算分页所需参数
	private void setPages() {
		if(!this.containsKey(PAGE_INDEX) || !this.containsKey(PAGE_SIZE)) return;

		Integer pageIndex = this.getPageIndex();
		Integer pageSize = this.getPageSize();

		int offset = pageIndex * pageSize;
		int limit = pageSize;

		this.put(OFFSET, offset);
		this.put(LIMIT, limit);
	}

	private void setPagesV2(int reduce) {
		if(!this.containsKey(PAGE_INDEX) || !this.containsKey(PAGE_SIZE)) return;

		Integer pageIndex = this.getPageIndex();
		Integer pageSize = this.getPageSize();

		int offset = (pageIndex * pageSize)-reduce;
		int limit = pageSize;

		this.put(OFFSET, offset);
		this.put(LIMIT, limit);
	}

	/**添加一个值,返回对象本身
	 * @author guoyu
	 * @param key
	 * @param value
	 * @return
	 */
	public ParamMap add(String key, Object value){
		this.put(key, value);
		return this;
	}

	public boolean isNullOrEmpty(String key){
		if (key == null || "".equals(key)) {
			return true;
		}
		String value = this.getString(key);
		if(value == null || "".equals(value) || "null".equals(value)){
			return true;
		}
		return false;
	}

	public boolean isNullOrEmpty(String... keyArr){
		if (keyArr == null || keyArr.length == 0) {
			return true;
		}
		for(String key : keyArr) {
			if (isNullOrEmpty(key)) {
				return true;
			}
		}
		return false;
	}

	public Object convertsObject(Class<?> clazz) {
		Object targetObj = null;
		try {
			targetObj = clazz.newInstance();
			Field[] fieldArr = clazz.getDeclaredFields();

			for(int i=0;i<fieldArr.length;i++) {
				Field field = fieldArr[i];

				//26代表 常量
				if(field.getModifiers() == 26) {
					// System.out.println(field.getName() + "是常量");
					continue;
				}
				//10 代表静态
				if(field.getModifiers() == 10) {
					// System.out.println(field.getName() + "是静态");
					continue;
				}
				field.setAccessible(true);
				field.set(targetObj, this.resultTypeValue(field.getType(), field.getName()));
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return targetObj;
	}

	private Object resultTypeValue(Class<?> clazz, String fieldName) {
		Object result = null;
		if(clazz == Long.class) {
			result = this.getLong(fieldName);
		}else if(clazz == String.class) {
			result = this.getString(fieldName);
		}else if(clazz == Integer.class){
			result = this.getInt(fieldName);
		}else if(clazz == Byte.class){
			result = this.getByte(fieldName);
		}else if(clazz == Character.class){
			result = this.getCharacter(fieldName);
		}else if(clazz == Short.class){
			result = this.getShort(fieldName);
		}else if(clazz == Boolean.class){
			result = this.getBool(fieldName);
		}else if(clazz == Float.class){
			result = this.getFloat(fieldName);
		}else if(clazz == Double.class){
			result = this.getDouble(fieldName);
		}else if(clazz == java.util.Date.class) {
			result = this.getDate(fieldName);
		}else {
			result = this.get(fieldName);
		}
		return result;
	}
}
