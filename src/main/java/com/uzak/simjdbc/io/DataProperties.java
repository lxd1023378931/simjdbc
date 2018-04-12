package com.uzak.simjdbc.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.uzak.simjdbc.util.PatternUtil;

/**
 * 读取properties
 * 
 * @author 梁秀斗
 * @mail 1023378931@qq.com
 * @date 2018年4月11日
 */
public class DataProperties {
	private Properties properties = null;
	private InputStream inputStream = null;
	private String path = null;

	public DataProperties(String path) {
		this.path = path;
		load();
	}

	private void load() {
		try {
			properties = new Properties();
			inputStream = DataProperties.class.getClassLoader().getResourceAsStream(path);
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Object get(String key) {
		if (properties == null) {
			load();
		}
		if (properties == null) {
			return new NullPointerException("读取配置文件失败!");
		}
		return properties.get(key);
	}

	public String getString(String key) {
		Object o = get(key);
		if (o != null) {
			return o.toString();
		} else {
			return "";
		}
	}

	public int getInt(String key) {
		Object o = get(key);
		if (o != null && PatternUtil.decide(o.toString(), PatternUtil.DIGITAL)) {
			return Integer.parseInt(o.toString());
		} else {
			return 0;
		}
	}

	public long getLong(String key) {
		Object o = get(key);
		if (o != null && PatternUtil.decide(o.toString(), PatternUtil.DIGITAL)) {
			return Long.parseLong(o.toString());
		} else {
			return 0L;
		}
	}

	public float getFloat(String key) {
		Object o = get(key);
		if (o != null && PatternUtil.decide(o.toString(), PatternUtil.DECIMAL)) {
			return Float.parseFloat(o.toString());
		} else {
			return 0.0F;
		}
	}

	public boolean getBoolean(String key) {
		Object o = get(key);
		if (o == null) {
			return false;
		}
		if ("true".equals(o.toString()) || "1".equals(o.toString())) {
			return true;
		}
		return false;
	}
}
