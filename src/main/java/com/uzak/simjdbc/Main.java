package com.uzak.simjdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.uzak.simjdbc.jdbc.DataPool;

public class Main {
	public static void main(String[] args) throws SQLException {
		DataPool dataPool = new DataPool();
		Connection conn = dataPool.getConnection();
		Statement state = conn.createStatement();
		ResultSet result = state.executeQuery("select * from uzteacher");
		while (result.next()) {
			System.out.println(result.getString("name"));
		}
		result.close();
		state.close();
		dataPool.releaseConnection(conn);
	}
}
