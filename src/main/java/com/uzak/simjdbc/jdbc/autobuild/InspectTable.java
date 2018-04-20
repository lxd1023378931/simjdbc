package com.uzak.simjdbc.jdbc.autobuild;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.uzak.simjdbc.io.DatabaseXml;
import com.uzak.simjdbc.jdbc.annocation.Column;
import com.uzak.simjdbc.jdbc.annocation.Table;
import com.uzak.simjdbc.jdbc.dao.BaseDao;
import com.uzak.simjdbc.jdbc.pool.DataPool;
import com.uzak.simjdbc.jdbc.transtion.Q;
import com.uzak.simjdbc.jdbc.transtion.SuperTranstion;
import com.uzak.simjdbc.util.StringUtil;

/**
 * 创建或更新表
 * 
 * @author 梁秀斗
 * @mail 1023378931@qq.com
 * @date 2018年4月21日
 */
public class InspectTable extends SuperTranstion {
	/**
	 * 查询表是否存在，如果存在则检查更新，否则创建新表
	 * 
	 * @param clazz
	 */
	public void inspect(Class<? extends BaseDao> clazz) {
		if (!clazz.isAnnotationPresent(Table.class)) {
			throw new AnnotationFormatError("该实体类缺少@Table注解！");
		}
		String tableName = clazz.getAnnotation(Table.class).name();
		String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema=? AND table_name = ?";
		Q q = new Q(sql, DatabaseXml.getDataBases().get(DataPool.DEFAULT_POOLNAME).getName(), tableName);
		int count = q.count();
		if (count == 0) {
			createTable(clazz);
		} else {
			updateTable(clazz);
		}
	}

	/**
	 * 根据实体类创建新表
	 * 
	 * @param clazz
	 */
	private void createTable(Class<? extends BaseDao> clazz) {
		try {
			Q q = new Q("CREATE TABLE IF NOT EXISTS " + clazz.getAnnotation(Table.class).name() + " ( ");
			Field[] fields = clazz.getDeclaredFields();
			int i = 0;
			for (Field field : fields) {
				Column column = field.getAnnotation(Column.class);
				if (!checkGetSet(clazz, field) || column == null) {
					// 如果缺少get（只传本身类型的参数）或set方法或者属性未添加@Column注解，则跳过
					continue;
				}
				String name = StringUtil.isEmpty(column.name()) ? field.getName() : column.name();
				String type = column.type() == ColumnType.NULL ? ColumnType.getName(field.getType()) : column.type().getName();
				// 如果是varchar,length 不能为0，其他为0
				int length = !type.equals(ColumnType.VARCHAR.getName()) ? 0 : column.length() == 0 ? 255 : column.length();
				if (length != 0) {
					type = type + "(" + length + ")";
				}
				boolean primary = column.primary();
				boolean notnull = column.notnull();
				boolean unique = column.unique();
				if (i == 0) {
					q.append("" + name + " ");
					i++;
				} else {
					q.append("," + name + " ");
				}
				q.append(type + " ");
				if (primary) {
					q.append(" primary key");
					continue;
				}
				if (notnull) {
					q.append(" not null ");
				}
				if (unique) {
					q.append(" unique ");
				}
			}
			q.append(")ENGINE=InnoDB DEFAULT CHARSET=utf8;");
			q.executeInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询某个参数是否存在get和set(T param)方法
	 * 
	 * @param clazz
	 * @param field
	 * @return
	 */
	private boolean checkGetSet(Class<? extends BaseDao> clazz, Field field) {
		String getMethod = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
		String setMethod = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
		try {
			Method method1 = clazz.getMethod(setMethod, field.getType());
			Method method2 = clazz.getMethod(getMethod);
			if (method1 == null) {
				throw new NoSuchMethodException("未查询到参数" + field.getName() + "的set方法");
			}
			if (method2 == null) {
				throw new NoSuchMethodException("未查询到参数" + field.getName() + "的get方法");
			}
			return true;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 更新表结构
	 * 
	 * @param clazz
	 */
	private void updateTable(Class<? extends BaseDao> clazz) {
		getTableColumn(clazz.getSimpleName());
	}

	private List<ColumnAttribute> getTableColumn(String tableName) {
		Connection conn = null;
		PreparedStatement state = null;
		ResultSet result = null;
		try {
			String sql = "desc " + tableName;
			conn = dataPool.getConnection();
			System.out.println("Execute SQL:" + sql.toString());
			state = conn.prepareStatement(sql.toString());
			result = state.executeQuery();
			return getTableColumn(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<ColumnAttribute> getTableColumn(ResultSet result) {
		List<ColumnAttribute> columnAttributes = new ArrayList<>();
		if (result == null) {
			return columnAttributes;
		}
		try {
			while (result.next()) {
				ColumnAttribute attribute = new ColumnAttribute();
				attribute.setField(result.getString("Field"));
				attribute.setType(result.getString("Type"));
				attribute.setNull(result.getString("Null"));
				attribute.setKey(result.getString("Key"));
				attribute.setDefault(result.getString("Default"));
				attribute.setExtra(result.getString("Extra"));
				columnAttributes.add(attribute);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return columnAttributes;
	}
}
