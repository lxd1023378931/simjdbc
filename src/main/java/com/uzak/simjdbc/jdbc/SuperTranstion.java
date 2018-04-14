package com.uzak.simjdbc.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class SuperTranstion {
	static DataPool dataPool = new DataPool();

	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);

	void setStateParams(PreparedStatement state, List<Object> params) {
		try {
			int i = 1;
			for (Object o : params) {
				if (o instanceof Number) {
					if (o.toString().contains(".")) {
						state.setDouble(i, Double.parseDouble(o.toString()));
					} else {
						state.setLong(i, Long.parseLong(o.toString()));
					}
				} else if (o instanceof Date) {
					state.setString(i, sdf.format(o));
				} else {
					state.setString(i, o.toString());
				}
				i++;
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
	}

}
