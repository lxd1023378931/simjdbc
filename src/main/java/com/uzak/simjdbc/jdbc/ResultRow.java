package com.uzak.simjdbc.jdbc;

import java.util.HashMap;
import java.util.Map;

public class ResultRow {
	private Map<String, Object> data = new HashMap<>();

	public ResultRow(Map<String, Object> data) {
		this.data = data;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public Object get(String key) {
		return data.get(key);
	}

	public String getString(String key) {
		return get(key) == null ? "" : get(key).toString();
	}

	public int getInt(String key) {
		return get(key) == null ? 0 : Integer.parseInt(getString(key));
	}

	public long getLong(String key) {
		return get(key) == null ? 0L : Long.parseLong(getString(key));
	}

	public double getDouble(String key) {
		return get(key) == null ? 0D : Double.parseDouble(getString(key));
	}

	public boolean getBoolean(String key) {
		return get(key) == null ? false : (getString(key).equals("true")?true:(getInt(key) == 1?true:false));
	}
}
