package com.uzak.simjdbc.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.uzak.simjdbc.jdbc.dao.IBaseDao;

/**
 * @author 梁秀斗
 * @mail 1023378931@qq.com
 * @date 2018年4月14日
 */
public class Transtion implements SuperTranstion {
	private List<Q> querys = new ArrayList<Q>();
	private Connection conn = null;
	public static final int INSERT = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;

	public Transtion() {
	}

	public void addQ(Q q) {
		querys.add(q);
		Collections.sort(querys, new Comparator<Q>() {
			@Override
			public int compare(Q q1, Q q2) {
				return q1.sql().replaceAll(" ", "").equals(q2.sql().replaceAll(" ", "")) ? 0 : -1;
			}
		});
	}

	public void addT(IBaseDao entrty, int type) {
		if (type == INSERT) {
			querys.add(entrty.insertQ());
		} else if (type == UPDATE) {
			querys.add(entrty.updateQ());
		} else if (type == DELETE) {
			querys.add(entrty.deleteQ());
		}
	}

	public void insert(IBaseDao entrty) {
		querys.add(entrty.insertQ());
	}

	public void update(IBaseDao entrty) {
		querys.add(entrty.updateQ());
	}

	public void delete(IBaseDao entrty) {
		querys.add(entrty.deleteQ());
	}

	public boolean commit() {
		return commit(DataPool.DEFAULT_POOLNAME);
	}

	public boolean commit(String poolName) {
		try {
			conn = dataPool.getConnection(poolName);
			conn.setAutoCommit(false);
			PreparedStatement state = null;
			String lastSql = "";
			for (Q q : querys) {
				System.out.println("Execute SQL:" + q.sql()+" "+q.params());
				if (!lastSql.equals(q.sql().replaceAll(" ", ""))) {
					lastSql = q.sql().replaceAll(" ", "");
					state = conn.prepareStatement(q.sql());
				}
				int i = 1;
				for (Object o : q.params()) {
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
				state.addBatch();
				state.executeBatch();
			}
			conn.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		return false;
	}

}
