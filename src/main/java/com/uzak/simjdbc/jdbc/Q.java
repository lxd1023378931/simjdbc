package com.uzak.simjdbc.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Q {
	private StringBuilder sql = new StringBuilder("");
	private List<Object> params = new ArrayList<>();
	DataPool dataPool = new DataPool();

	public Q() {
	}

	public Q(String sql, Object... param) {
		this.sql.append(sql);
		for (Object p : param) {
			params.add(p);
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

	public ResultList executeResultList() {
		return executePageResultList(0, 0);
	}

	public ResultList executePageResultList(int pageIndex, int pageSize) {
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

			conn = dataPool.getConnection();
			state = conn.prepareStatement(sql.toString());
			int i = 1;
			for (Object o : params) {
				if (o instanceof Number) {
					if (o.toString().contains(".")) {
						state.setDouble(i, ((Number) o).doubleValue());
					} else {
						state.setLong(i, ((Number) o).longValue());
					}
				} else {
					state.setString(i, o.toString());
				}
				i++;
			}
			result = state.executeQuery();
			resultList = new ResultList(result);
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
		return resultList;
	}

	public int executeInt() {
		Connection conn = null;
		PreparedStatement state = null;
		int index = 0;
		try {
			conn = dataPool.getConnection();
			state = conn.prepareStatement(sql.toString());
			int i = 1;
			for (Object o : params) {
				if (o instanceof Number) {
					state.setFloat(i, ((Number) o).floatValue());
				} else {
					state.setString(i, o.toString());
				}
				i++;
			}
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
		Connection conn = null;
		PreparedStatement state = null;
		ResultSet result = null;
		String s = null;
		try {
			conn = dataPool.getConnection();
			state = conn.prepareStatement(sql.toString());
			int i = 1;
			for (Object o : params) {
				if (o instanceof Number) {
					state.setFloat(i, ((Number) o).floatValue());
				} else {
					state.setString(i, o.toString());
				}
				i++;
			}
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
		Connection conn = null;
		PreparedStatement state = null;
		ResultSet result = null;
		int count = 0;
		try {
			String sqll = sql.toString();
			System.out.println(sqll);
			Pattern pattern = Pattern.compile("select(.*?)from");
			Matcher m = pattern.matcher(sqll);  
	        if(m.find()){  
	        	sqll = sqll.substring(0, sqll.indexOf(m.group(1)))+" count(1) "+sqll.substring(sqll.indexOf(m.group(1))+m.group(1).length());
	        }  
			System.out.println(sqll);
			conn = dataPool.getConnection();
			state = conn.prepareStatement(sqll);
			int i = 1;
			for (Object o : params) {
				if (o instanceof Number) {
					state.setFloat(i, ((Number) o).floatValue());
				} else {
					state.setString(i, o.toString());
				}
				i++;
			}
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