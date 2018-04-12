package com.uzak.simjdbc.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.uzak.simjdbc.exception.NoDatabaseException;
import com.uzak.simjdbc.io.DatabaseXml;

/**
 * 多连接池管理
 * 
 * @author 梁秀斗
 * @mail 1023378931@qq.com
 * @date 2018年4月12日
 */
public class DataPool implements DataSource {
	private static Map<String, LinkedList<Connection>> pools = new HashMap<String, LinkedList<Connection>>();
	private static Map<String, DataBase> databases = DatabaseXml.getDataBases();
	private static final String DEFAULT_POOLNAME = "default";
	// 默认等待时间
	private static final long DEFAULT_TIMEOUT = 5;

	public DataPool() {
		synchronized (databases) {
			databases.forEach((id, db) -> {
				LinkedList<Connection> conns = new LinkedList<>();
				for (int i = 0; i < db.getInitPoolCount(); i++) {
					conns.add(createConnection(db));
				}
				pools.put(id, conns);
				db.setCount(db.getInitPoolCount());
				databases.put(id, db);
			});
		}
	}

	private synchronized Connection createConnection(DataBase db) {
		try {
			Class.forName(db.getDriver());
			Connection conn = DriverManager.getConnection(getUrl(db), db.getUserName(), db.getPassword());
			db.setCount(db.getCount() + 1);
			return conn;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取默认连接池的连接
	 */
	public Connection getConnection() throws SQLException {
		return getConnection(DEFAULT_POOLNAME);
	}

	/**
	 * 获取指定连接池的连接
	 * 
	 * @param poolName
	 * @return
	 * @throws SQLException
	 */
	public synchronized Connection getConnection(String poolName) throws SQLException {
		try {
			LinkedList<Connection> pool = pools.get(poolName);
			DataBase db = databases.get(poolName);
			if (pool == null) {
				throw new NoDatabaseException("未找到'" + poolName + "'连接配置");
			}
			Connection conn = pool.removeFirst();
			if (conn == null) {
				if (db.getCount() < db.getMaxPoolCount()) {
					conn = createConnection(db);
				} else {
					long timeout = db.getLoginTimeout() == 0 ? DEFAULT_TIMEOUT : db.getLoginTimeout();
					long time = System.currentTimeMillis() + timeout * 1000;
					while (System.currentTimeMillis() < time) {
						conn = pool.removeFirst();
						if (conn != null) {
							break;
						}
					}
				}
			}
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 归还连接到默认连接池
	 * 
	 * @param conn
	 */
	public synchronized void releaseConnection(Connection conn) {
		releaseConnection(DEFAULT_POOLNAME, conn);
	}

	/**
	 * 归还连接
	 * 
	 * @param poolName
	 * @param conn
	 */
	public synchronized void releaseConnection(String poolName, Connection conn) {
		try {
			LinkedList<Connection> pool = pools.get(poolName);
			DataBase db = databases.get(poolName);
			if (pool == null) {
				throw new NoDatabaseException("未找到'" + poolName + "'连接配置");
			}
			if (db.getCount() >= db.getMaxPoolCount()) {
				conn.close();
				return;
			}
			pool.add(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getUrl(DataBase db) {
		String url = "";
		if("mysql".equalsIgnoreCase(db.getType())) {
			url = "jdbc:mysql://" + db.getHost() + ":" + db.getPort() + "/" + db.getName()
			+ "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
		}
		// else if("oracle".equalsIgnoreCase(db.getType())) {
		// url = "jdbc:oracle:thin:@"+db.getHost()+":"+db.getPort()+":"+db.getName();
		// }
		return url;
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}

	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	public void setLoginTimeout(int seconds) throws SQLException {
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

}
