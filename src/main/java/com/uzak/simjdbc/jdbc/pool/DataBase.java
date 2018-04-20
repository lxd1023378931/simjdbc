package com.uzak.simjdbc.jdbc.pool;

/**
 * 数据库连接对象
 * 
 * @author 梁秀斗
 * @mail 1023378931@qq.com
 * @date 2018年4月12日
 */
public class DataBase {
	private String type;
	private String driver;
	private String host;
	private String port;
	private String name;
	private String userName;
	private String password;
	private int initPoolCount;
	private int maxPoolCount;
	private long loginTimeout;

	private int count;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getInitPoolCount() {
		return initPoolCount;
	}

	public void setInitPoolCount(int initPoolCount) {
		this.initPoolCount = initPoolCount;
	}

	public int getMaxPoolCount() {
		return maxPoolCount;
	}

	public void setMaxPoolCount(int maxPoolCount) {
		this.maxPoolCount = maxPoolCount;
	}

	public long getLoginTimeout() {
		return loginTimeout;
	}

	public void setLoginTimeout(long loginTimeout) {
		this.loginTimeout = loginTimeout;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
