package com.uzak.simjdbc.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 梁秀斗
 * @mail 1023378931@qq.com
 * @date 2018年4月14日
 */
public class Q extends SuperTranstion {
	private StringBuilder sql = null;
	private List<Object> params = new ArrayList<>();

	public Q() {
		this.sql = new StringBuilder("");
	}

	public Q(String sql, Object... params) {
		this.sql = new StringBuilder(sql);
		for (Object p : params) {
			this.params.add(p);
		}
	}

	public void append(String sql, Object... param) {
		this.sql.append(" " + sql + " ");
		for (Object p : params) {
			this.params.add(p);
		}
	}

	public String sql() {
		return sql.toString();
	}

	public List<Object> params() {
		return params;
	}

	public void where() {
		sql.append(" where 1=1 ");
	}

	public void where(String key, Object value) {
		sql.append(" where ").append(key).append(" = ? ");
		if (value != null) {
			params.add(value);
		}
	}

	public void and() {
		sql.append(" and ");
	}

	public void and(String key, Object value) {
		sql.append(" and ").append(key).append(" = ? ");
		if (value != null) {
			params.add(value);
		}
	}

	public void eq(String key, Object value) {
		sql.append(" ").append(key).append(" = ? ");
		if (value != null) {
			params.add(value);
		}
	}

	public void ne(String key, Object value) {
		sql.append(" ").append(key).append(" <> ? ");
		if (value != null) {
			params.add(value);
		}
	}

	public void gt(String key, Object value) {
		sql.append(" ").append(key).append(" > ? ");
		if (value != null) {
			params.add(value);
		}
	}

	public void lt(String key, Object value) {
		sql.append(" ").append(key).append(" < ? ");
		if (value != null) {
			params.add(value);
		}
	}

	public void ge(String key, Object value) {
		sql.append(" ").append(key).append(" >= ? ");
		if (value != null) {
			params.add(value);
		}
	}

	public void le(String key, Object value) {
		sql.append(" ").append(key).append(" <= ? ");
		if (value != null) {
			params.add(value);
		}
	}

	public void like(String key, String value) {
		sql.append(" ").append(key).append(" like ? ");
		if (value != null) {
			params.add("%" + value.toString() + "%");
		}
	}

	public void leftLike(String key, String value) {
		sql.append(" ").append(key).append(" like ? ");
		if (value != null) {
			params.add("%" + value.toString());
		}
	}

	public void rightLike(String key, String value) {
		sql.append(" ").append(key).append(" like ? ");
		if (value != null) {
			params.add(value.toString() + "%");
		}
	}

	public void orderby(String value) {
		sql.append(" order by ? ASC ");
		if (value != null) {
			params.add(value.toString() + "%");
		}
	}

	public void orderby(String value, int sort) {
		sql.append(" order by ? ");
		if (sort == 1) {
			sql.append("DESC");
		} else {
			sql.append("ASC");
		}
		if (value != null) {
			params.add(value.toString() + "%");
		}
	}

	public ResultList executeResultList() {
		return executePageResultList(0, 0);
	}

	public ResultList executeResultList(String poolName) {
		return executePageResultList(0, 0, poolName);
	}

	public ResultList executePageResultList(int pageIndex, int pageSize) {
		return executePageResultList(pageIndex, pageSize, DataPool.DEFAULT_POOLNAME);
	}

	public ResultList executePageResultList(int pageIndex, int pageSize, String poolName) {
		Connection conn = null;
		PreparedStatement state = null;
		ResultSet result = null;
		ResultList resultList = new ResultList();
		try {

			if (pageSize != 0) {
				sql.append(" limit ?,? ");
				params.add(pageIndex == 0 ? 0 : pageIndex * pageSize);
				params.add(pageSize);
			}

			conn = dataPool.getConnection(poolName);
			System.out.println("Execute SQL:" + sql.toString() + " " + params);
			state = conn.prepareStatement(sql.toString());
			setStateParams(state, params);
			// int i = 1;
			// for (Object o : params) {
			// if (o instanceof Number) {
			// if (o.toString().contains(".")) {
			// state.setDouble(i, ((Number) o).doubleValue());
			// } else {
			// state.setLong(i, ((Number) o).longValue());
			// }
			// } else {
			// state.setString(i, o.toString());
			// }
			// i++;
			// }
			result = state.executeQuery();
			resultList = new ResultList(result);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				SuperTranstion.dataPool.releaseConnection(conn);
			}
			try {
				if (state != null) {
					state.close();
				}
				if (result != null) {
					result.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	public int executeInt() {
		return executeInt(DataPool.DEFAULT_POOLNAME);
	}

	public int executeInt(String poolName) {
		Connection conn = null;
		PreparedStatement state = null;
		int index = 0;
		try {
			conn = SuperTranstion.dataPool.getConnection(poolName);
			System.out.println("Execute SQL:" + sql.toString() + " " + params);
			state = conn.prepareStatement(sql.toString());
			setStateParams(state, params);
			// int i = 1;
			// for (Object o : params) {
			// if (o instanceof Number) {
			// if (o.toString().contains(".")) {
			// state.setDouble(i, ((Number) o).doubleValue());
			// } else {
			// state.setLong(i, ((Number) o).longValue());
			// }
			// } else {
			// state.setString(i, o.toString());
			// }
			// i++;
			// }
			index = state.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				dataPool.releaseConnection(conn);
			}
			try {
				if (state != null) {
					state.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return index;
	}

	public String executeString() {
		return executeString(DataPool.DEFAULT_POOLNAME);
	}

	public String executeString(String poolName) {
		Connection conn = null;
		PreparedStatement state = null;
		ResultSet result = null;
		String s = null;
		try {
			conn = dataPool.getConnection(poolName);
			System.out.println("Execute SQL:" + sql.toString() + " " + params);
			state = conn.prepareStatement(sql.toString());
			setStateParams(state, params);
			// int i = 1;
			// for (Object o : params) {
			// if (o instanceof Number) {
			// if (o.toString().contains(".")) {
			// state.setDouble(i, ((Number) o).doubleValue());
			// } else {
			// state.setLong(i, ((Number) o).longValue());
			// }
			// } else {
			// state.setString(i, o.toString());
			// }
			// i++;
			// }
			result = state.executeQuery();
			if (result.next()) {
				s = result.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				dataPool.releaseConnection(conn);
			}
			try {
				if (state != null) {
					state.close();
				}
				if (result != null) {
					result.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return s;
	}

	public int count() {
		return count(DataPool.DEFAULT_POOLNAME);
	}

	public int count(String poolName) {
		Connection conn = null;
		PreparedStatement state = null;
		ResultSet result = null;
		int count = 0;
		try {
			String sqll = sql.toString();
			Pattern pattern = Pattern.compile("select(.*?)from");
			Matcher m = pattern.matcher(sqll);
			if (m.find()) {
				sqll = sqll.substring(0, sqll.indexOf(m.group(1))) + " count(1) "
						+ sqll.substring(sqll.indexOf(m.group(1)) + m.group(1).length());
			}
			conn = dataPool.getConnection(poolName);
			System.out.println("Execute SQL:" + sql.toString() + " " + params);
			state = conn.prepareStatement(sqll);
			setStateParams(state, params);
			// int i = 1;
			// for (Object o : params) {
			// if (o instanceof Number) {
			// if (o.toString().contains(".")) {
			// state.setDouble(i, ((Number) o).doubleValue());
			// } else {
			// state.setLong(i, ((Number) o).longValue());
			// }
			// } else {
			// state.setString(i, o.toString());
			// }
			// i++;
			// }
			result = state.executeQuery();
			if (result.next()) {
				count = result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				dataPool.releaseConnection(conn);
			}
			try {
				if (state != null) {
					state.close();
				}
				if (result != null) {
					result.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}
}
