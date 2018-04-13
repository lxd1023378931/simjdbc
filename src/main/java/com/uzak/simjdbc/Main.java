package com.uzak.simjdbc;

import java.sql.SQLException;
import java.util.Date;

import com.uzak.simjdbc.jdbc.Q;
import com.uzak.simjdbc.jdbc.Transtion;

import schame.Test;

public class Main {
	public static void main(String[] args) throws SQLException {
		// Transtion trans = new Transtion();
		// Q q1 = new Q("update test set name=? where id=?", "admin1", 0);
		// trans.addQ(q1);
		// Test test = new Test();
		// test.setId(2);
		// test.setName("123");
		// trans.insert(test);
		// Test test1 = new Test();
		// test1.setId(1);
		// test1.setName("e");
		// trans.delete(test1);
		// if (trans.commit()) {
		// System.out.println("执行成功");
		// }
		Test test = new Test();
		test.setId(1);
		test.setName("dasfa");
		test.insert();
		
		Test test1 = new Test();
		test1.setId(1);
		test1.fill();
		System.out.println(test1.getName());
	}
}
