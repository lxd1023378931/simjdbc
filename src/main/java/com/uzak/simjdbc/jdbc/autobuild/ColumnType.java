package com.uzak.simjdbc.jdbc.autobuild;

import java.util.Date;

/**
 * 字段类型
 * 
 * @author 梁秀斗
 * @mail 1023378931@qq.com
 * @date 2018年4月20日
 */
public enum ColumnType {
	NULL("", null), INT("int", Integer.class), BIGINT("bigint", Long.class), BIT("bit", Boolean.class), DOUBLE("double",
			Double.class), FLOAT("float", Float.class), VARCHAR("varchar",
					String.class), DATETIME("datetime", Date.class), BLOB("blob", String.class), CLOB("clob", String.class);

	private String name;
	private Class<?> type;

	private ColumnType(String name, Class<?> type) {
		this.setName(name);
		this.setType(type);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public static Class<?> getType(String name) {
		for (ColumnType columnType : ColumnType.values()) {
			if (columnType.getName().equals(name)) {
				return columnType.getType();
			}
		}
		return null;
	}

	public static String getName(Class<?> clazz) {
		for (ColumnType columnType : ColumnType.values()) {
			if (columnType == NULL) {
				continue;
			}
			if (columnType.getType().getName().equals(clazz.getName())) {
				return columnType.getName();
			}
		}
		return VARCHAR.getName();
	}
}
