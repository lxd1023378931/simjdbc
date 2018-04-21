package com.uzak.simjdbc.jdbc.autobuild;

/**
 * 表属性
 * 
 * @author 梁秀斗
 * @mail 1023378931@qq.com
 * @date 2018年4月20日
 */
public class ColumnAttribute {
	private String Field;
	private String Type;
	private int length;
	private boolean NotNull;
	private boolean Key;
	private boolean unique;
	private String Default;
	private String Extra;

	public String getField() {
		return Field;
	}

	public void setField(String field) {
		Field = field;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public boolean getKey() {
		return Key;
	}

	public void setKey(boolean key) {
		Key = key;
	}

	public String getDefault() {
		return Default;
	}

	public void setDefault(String default1) {
		Default = default1;
	}

	public String getExtra() {
		return Extra;
	}

	public void setExtra(String extra) {
		Extra = extra;
	}

	public boolean getUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean getNotNull() {
		return NotNull;
	}

	public void setNotNull(boolean notNull) {
		NotNull = notNull;
	}

}
