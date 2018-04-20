package com.uzak;

import java.sql.SQLException;

import com.uzak.schame.Test;
import com.uzak.simjdbc.jdbc.autobuild.InspectTable;

public class Main {
	public static void main(String[] args) throws SQLException {
		InspectTable inspectTable = new InspectTable();
		inspectTable.inspect(Test.class);
	}
}
