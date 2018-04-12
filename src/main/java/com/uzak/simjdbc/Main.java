package com.uzak.simjdbc;

import java.sql.SQLException;

import com.uzak.simjdbc.jdbc.Q;

public class Main {
	public static void main(String[] args) throws SQLException {
		// DataPool dataPool = new DataPool();
		// Connection conn = dataPool.getConnection();
		// Statement state = conn.createStatement();
		// ResultSet result = state.executeQuery("select * from uzteacher");
		// while (result.next()) {
		// System.out.println(result.getString("name"));
		// }
		// result.close();
		// state.close();
		// dataPool.releaseConnection(conn);
		Q q = new Q("select * from uzteacher");
		// ResultList rl = q.executePageResultList(1, 2);
		// for (ResultRow rr : rl.getData()) {
		// System.out.println(rr.getString("name"));
		// }
		System.out.println(q.count());
	}
}
