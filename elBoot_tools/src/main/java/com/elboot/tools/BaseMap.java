package com.elboot.tools;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;



public class BaseMap extends HashMap<String, Object> implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public BaseMap() {

	}

	@Override
	public Object put(String key, Object value){
		if(value instanceof java.sql.Date){
			java.sql.Date sdate = (java.sql.Date)value;
			Date udate = new Date();
			udate.setTime(sdate.getTime());
			super.put(key, udate);
		}else{
			super.put(key, value);
		}
		return value;
	}

	public void convertsInt(String...keys){
		for(String key: keys){
			if(this.containsKey(key)) {
				Object value = this.getInt(key);
				this.setProperty(key, value);
			}
		}
	}

	public void convertsLike(String...keys){
		for(String key: keys){
			if(this.containsKey(key)) {
				Object value = this.getLikeValue(key);
				this.setProperty(key, value);
			}
		}
	}

	public void dateBefore(String key, int before) {
		Date date = this.getDate(key);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, before);
		date = cal.getTime();

		this.setProperty(key, date);
	}

	public void setBeginDateTimeBefore(String key, Date value, int before) {
		Date date = value;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 0-before);
		date = cal.getTime();

		this.setProperty(key, date);

		String dateStr = this.getDateString(key);
		dateStr += " 00:00:00";

		this.setProperty(key, dateStr);
	}

	public void setEndDateTimeBefore(String key, Date value, int before) {
		Date date = value;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 0-before);
		date = cal.getTime();

		this.setProperty(key, date);

		String dateStr = this.getTomorrowDateString(key);
		dateStr += " 00:00:00";

		this.setProperty(key, dateStr);
	}

	public void setBeginDateTime(String key) {
		this.setBeginDateTimeStr(key);
		this.convertsDateTime(key);
	}

	public void setBeginDateTimeStr(String key) {
		String value = this.getDateString(key);
		value += " 00:00:00";

		this.setProperty(key, value);
	}

	public void setEndDateTime(String key) {
		this.setEndDateTimeStr(key);
		this.convertsDateTime(key);
	}

	public void setEndDateTimeStr(String key) {
		String value = this.getTomorrowDateString(key);
		value += " 00:00:00";

		this.setProperty(key, value);
	}

	public void setProperty(String key, Object value){
		this.put(key, value);
	}

	public BaseMap set(String key, Object value){
		this.put(key, value);
		return this;
	}

	public void setKeys(String...keys){
		for(String key : keys){
			this.setProperty(key, null);
		}
	}

	public String getLeftLikeValue(String key) {
		String value = this.getString(key);
		try {
			value = java.net.URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		value = value+"%";
		return value;
	}

	public String getLikeValue(String key) {
		String value = this.getString(key);
        if(value == null) return null;
		try {
			 value = java.net.URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		value = "%"+value+"%";
		return value;
	}

	public void emptyAllValues(){
		for(String key : this.keySet()){
			this.put(key, null);
		}
	}

    /**
     * 多个参数判断是否为空
     * @author guoyu
     */
    public Boolean isEmpty(String... keys){
        if(keys == null){
            return true;
        }

        for (String key : keys) {
            if(isEmpty(key)) return true;
        }

        return false;
    }

	public Boolean isEmpty(String key){
		if(super.containsKey(key)){
			Object value = super.get(key);
			if(value == null){
				return true;
			}else if("".equals(String.valueOf(value).trim())){
				return true;
			}
			return false;
		}
		return true;
	}

	public Boolean isNotEmpty(String key){
		return !this.isEmpty(key);
	}

	public Object getObject(String key){
		return super.get(key);
	}

	public <T> T getClsObject(String key, Class<T> objClass){
		return (T)this.get(key);
	}

	public String getString(String key) {
		if(!this.containsKey(key) || super.get(key) == null) return null;
		return super.get(key).toString();
	}

	public BigDecimal getDecimal(String key) {
		return new BigDecimal(super.get(key).toString());
	}

	public Integer getInt(String key){
		Object value = this.get(key);
		if("".equals(value) || value == null)return null;

		return Integer.parseInt(value.toString());
	}

	public Long getLong(String key){
		Object value = this.get(key);
		if("".equals(value) || value == null)return null;

		return Long.parseLong(value.toString());
	}

	public Double getDouble(String key){
		Object value = this.get(key);
		if("".equals(value) || value == null)return null;
		return Double.parseDouble(this.getString(key));
	}

	public Short getShort(String key){
		Object value = this.get(key);
		if("".equals(value) || value == null)return null;
		return Short.parseShort(this.getString(key));
	}

	public Byte getByte(String key){
		Object value = this.get(key);
		if("".equals(value) || value == null)return null;
		return Byte.parseByte(this.getString(key));
	}

	public Character getCharacter(String key){
		Object value = this.get(key);
		if("".equals(value) || value == null) {
			return null;
		}
		if(this.getString(key).length() > 1) {
			return null;
		}
		return Character.valueOf(this.getString(key).charAt(0));
	}

	public Float getFloat(String key){
		return Float.parseFloat(this.getString(key));
	}

	public Boolean getBool(String key){
		return Boolean.parseBoolean(this.getString(key));
	}

	public void convertsDateTime(String...keys){
		for(String key: keys){
			if(this.isEmpty(key)) continue;

			Object value = this.get(key);
			String datePattern = "yyyy-MM-dd HH:mm:ss";

			value = this.getDate(key, datePattern);
			this.setProperty(key, value);
		}
	}

	public void convertsDate(String...keys){
		for(String key: keys){
			if(this.isEmpty(key)) continue;

			Object value = this.get(key);
			String datePattern = "yyyy-MM-dd";

			value = this.getDate(key, datePattern);
			this.setProperty(key, value);
		}
	}

	public Date getDate(String key, String datePattern){
		if(datePattern == null) datePattern = "yyyy-MM-dd";
		SimpleDateFormat df = new SimpleDateFormat(datePattern, Locale.UK);
		Object value = super.get(key);
		if(value == null){
			return null;
		}else if(value instanceof Date){
			return (Date)value;
		}else if(value instanceof String){
	    	try {
				return df.parse(this.getString(key));
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return (Date)value;
	}

	public Date getDate(String key){
		return this.getDate(key, null);
	}

	public String getDateString(String key){
		return this.getDateString(key, null);
	}

	public String getDateString(String key, String datePattern){
		if(datePattern == null) datePattern = "yyyy-MM-dd";
		SimpleDateFormat df = new SimpleDateFormat(datePattern, Locale.UK);
		Object value = super.get(key);
		if(value == null || "".equals(value)){
			return "";
		}else if(value instanceof Date){
			return df.format(value);
		}else if(value instanceof String){
			try {
				Date date = df.parse(this.getString(key));
				return df.format(date);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	public String getTomorrowDateString(String key) {
		String datePattern = "yyyy-MM-dd";
		SimpleDateFormat df = new SimpleDateFormat(datePattern, Locale.UK);
		Object value = super.get(key);
		Date date = new Date();

		if(value == null || "".equals(value)) {
			return "";
		}else if(value instanceof Date){
			date = (Date)value;
		}else if(value instanceof String){
			try {
				date = df.parse(this.getString(key));
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
		return df.format(cal.getTime());
	}


}
