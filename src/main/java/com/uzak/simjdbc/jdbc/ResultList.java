package com.uzak.simjdbc.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultList {
	private List<ResultRow> data = new ArrayList<>();
	private List<String> columns = new ArrayList<>();
	private Map<String, String> types = new HashMap<>();

	public ResultList() {
	}

	public ResultList(ResultSet rs) {
		try {
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columns.add(metaData.getColumnName(i));
				types.put(metaData.getColumnName(i), metaData.getColumnTypeName(i));
			}
			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				for (String column : columns) {
					row.put(column, rs.getObject(column));
				}
				ResultRow rr = new ResultRow(row);
				data.add(rr);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<ResultRow> getData() {
		return data;
	}

	public void setData(List<ResultRow> data) {
		this.data = data;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public Map<String, String> getTypes() {
		return types;
	}

	public void setTypes(Map<String, String> types) {
		this.types = types;
	}

}
