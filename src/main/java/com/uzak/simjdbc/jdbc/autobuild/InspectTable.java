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
import com.uzak.simjdbc.jdbc.transtion.Transtion;
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
		String tableName = getTableName(clazz);
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
			String tableName = getTableName(clazz);
			Q q = new Q("CREATE TABLE IF NOT EXISTS " + tableName + " ( ");
			List<ColumnAttribute> attributes = getClassColumn(clazz);
			int i = 0;
			for (ColumnAttribute attribute : attributes) {
				if (i == 0) {
					q.append("" + attribute.getField() + " ");
					i++;
				} else {
					q.append("," + attribute.getField() + " ");
				}
				if (attribute.getLength() != 0) {
					q.append(attribute.getType() + "(" + attribute.getLength() + ") ");
				} else {
					q.append(attribute.getType() + " ");
				}

				if (attribute.getKey()) {
					q.append(" primary key");
					continue;
				}
				if (attribute.getNotNull()) {
					q.append(" not null ");
				}
				if (attribute.getUnique()) {
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
	 * 更新表结构
	 * 
	 * @param clazz
	 */
	private void updateTable(Class<? extends BaseDao> clazz) {
		List<ColumnAttribute> tableAttributes = getTableColumn(getTableName(clazz));
		List<ColumnAttribute> classAttributes = getClassColumn(clazz);
		Transtion trans = new Transtion();
		String tableName = getTableName(clazz);
		for (ColumnAttribute classAttr : classAttributes) {
			boolean exist = false;
			for (ColumnAttribute tableAttr : tableAttributes) {
				if (classAttr.getField().equals(tableAttr.getField())) {
					// 找到同字段名的则检查更新
					trans.addQ(alterColumn(tableName, tableAttr, classAttr));
					// 移除被处理的列
					tableAttributes.remove(tableAttr);
					exist = true;
					break;
				}
			}
			if (!exist) {
				// 未找到则添加
				trans.addQ(addColumn(tableName, classAttr));
			}
		}
		for (ColumnAttribute tableAttr : tableAttributes) {
			// 剩下的则是无用列，将列名后面追加“_unused”
			trans.addQ(unusedColumn(tableName, tableAttr));
		}
		trans.commit();
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
	 * 获取数据库表结构
	 * 
	 * @param tableName
	 * @return
	 */
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

	/**
	 * 获取数据库表的列属性
	 * 
	 * @param result
	 * @return
	 */
	private List<ColumnAttribute> getTableColumn(ResultSet result) {
		List<ColumnAttribute> columnAttributes = new ArrayList<>();
		if (result == null) {
			return columnAttributes;
		}
		try {
			while (result.next()) {
				ColumnAttribute attribute = new ColumnAttribute();
				attribute.setField(result.getString("Field").toLowerCase());
				String type = result.getString("Type");
				String[] types = type.split("\\(");
				attribute.setType(types[0]);
				if (String.class.getName().equals(ColumnType.getType(types[0]).getName())) {
					if (StringUtil.isNotEmpty(types[1])) {
						attribute.setLength(Integer.parseInt(types[1].substring(0, types[1].length() - 1)));
					} else {
						attribute.setLength(0);
					}
				} else {
					attribute.setLength(0);
				}
				attribute.setKey("PRI".equals(result.getString("Key")) ? true : false);
				attribute.setNotNull(attribute.getKey() ? true : ("NO".equals(result.getString("Null")) ? true : false));
				attribute.setUnique(attribute.getKey() ? true : ("UNI".equals(result.getString("Key")) ? true : false));
				attribute.setDefault(result.getString("Default"));
				attribute.setExtra(result.getString("Extra"));
				columnAttributes.add(attribute);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return columnAttributes;
	}

	/**
	 * 获取实体类列属性
	 * 
	 * @param clazz
	 * @return
	 */
	private List<ColumnAttribute> getClassColumn(Class<? extends BaseDao> clazz) {
		List<ColumnAttribute> classAttribute = new ArrayList<>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			ColumnAttribute attribute = new ColumnAttribute();
			Column column = field.getAnnotation(Column.class);
			if (!checkGetSet(clazz, field) || column == null) {
				// 如果缺少get（只传本身类型的参数）或set方法或者属性未添加@Column注解，则跳过
				continue;
			}
			String name = StringUtil.isEmpty(column.name()) ? field.getName().toLowerCase() : column.name().toLowerCase();
			String type = column.type() == ColumnType.NULL ? ColumnType.getName(field.getType()) : column.type().getName();
			// 如果是varchar,length 不能为0，其他为0
			int length = !type.equals(ColumnType.VARCHAR.getName()) ? 0 : column.length() == 0 ? 255 : column.length();
			boolean primary = column.primary();
			boolean notnull = primary ? true : column.notnull();
			boolean unique = primary ? true : column.unique();
			attribute.setField(name);
			attribute.setType(type);
			attribute.setLength(length);
			attribute.setKey(primary);
			attribute.setNotNull(notnull);
			attribute.setUnique(unique);
			classAttribute.add(attribute);
		}
		return classAttribute;
	}

	/**
	 * 修改列属性
	 * 
	 * @param tableAttr
	 * @param classAttr
	 * @return
	 */
	private List<Q> alterColumn(String tableName, ColumnAttribute tableAttr, ColumnAttribute classAttr) {
		List<Q> qs = new ArrayList<>();
		if (tableAttr.getKey() != classAttr.getKey() || tableAttr.getNotNull() != classAttr.getNotNull()
				|| tableAttr.getUnique() != classAttr.getUnique() || !tableAttr.getType().equals(classAttr.getType())
				|| (tableAttr.getLength() != 0 && classAttr.getLength() != tableAttr.getLength())) {
			Q q = new Q("alter table " + tableName + " modify ");
			q.append(classAttr.getField() + " ");
			if (classAttr.getLength() != 0) {
				q.append(classAttr.getType() + "(" + classAttr.getLength() + ") ");
			} else {
				q.append(classAttr.getType() + " ");
			}

			if (classAttr.getKey() && !tableAttr.getKey()) {
				q.append(" primary key");
				qs.add(q);
				return qs;
			} else if (!classAttr.getKey() && tableAttr.getKey()) {
				q.append(",drop primary key");
				qs.add(q);
				return qs;
			}
			if (!classAttr.getKey()) {
				if (classAttr.getNotNull() && !tableAttr.getNotNull()) {
					q.append(" not null ");
				} else if (!classAttr.getNotNull() && tableAttr.getNotNull()) {
					q.append(" null ");
				}
				if (classAttr.getUnique() && !tableAttr.getUnique()) {
					q.append(" unique ");
				} else if (!classAttr.getUnique() && tableAttr.getUnique()) {
					if (tableAttr.getUnique()) {
						qs.add(new Q("alter table " + tableName + " drop index " + classAttr.getField()));
					}
				}
			}
			qs.add(q);
			return qs;
		}
		return qs;
	}

	/**
	 * 添加新列
	 * 
	 * @param classAttr
	 * @return
	 */
	private Q addColumn(String tableName, ColumnAttribute classAttr) {
		Q q = new Q("alter table " + tableName + " add column ");
		q.append(classAttr.getField() + " ");
		if (classAttr.getLength() != 0) {
			q.append(classAttr.getType() + "(" + classAttr.getLength() + ") ");
		} else {
			q.append(classAttr.getType() + " ");
		}

		if (classAttr.getKey()) {
			q.append(" primary key");
			return q;
		}
		if (classAttr.getNotNull()) {
			q.append(" not null ");
		}
		if (classAttr.getUnique()) {
			q.append(" unique ");
		}
		return q;
	}

	/**
	 * 设置无用列
	 * 
	 * @param tableAttr
	 * @return
	 */
	private Q unusedColumn(String tableName, ColumnAttribute tableAttr) {

		if (tableAttr.getField().endsWith("_unused")) {
			return null;
		}

		String newFieldName = getNewFieldName(tableName, tableAttr.getField() + "_unused");

		Q q = new Q("alter table " + tableName + " change ");
		q.append(tableAttr.getField() + " " + newFieldName + " ");
		if (tableAttr.getLength() != 0) {
			q.append(tableAttr.getType() + "(" + tableAttr.getLength() + ") ");
		} else {
			q.append(tableAttr.getType() + " ");
		}

		if (tableAttr.getKey()) {
			q.append(" primary key");
			return q;
		}
		if (tableAttr.getNotNull()) {
			q.append(" not null ");
		}
		if (tableAttr.getUnique()) {
			q.append(" unique ");
		}
		return q;
	}

	/**
	 * 获取未使用的新列名
	 * 
	 * @param tableName
	 * @param newName
	 * @return
	 */
	private String getNewFieldName(String tableName, String newName) {
		Q existName = new Q("SELECT  TABLE_NAME FROM information_schema.COLUMNS WHERE " + " TABLE_SCHEMA = '"
				+ DatabaseXml.getDataBases().get(DataPool.DEFAULT_POOLNAME).getName() + "'  AND TABLE_NAME = '" + tableName
				+ "'  AND COLUMN_NAME = '" + newName + "'");
		if (existName.count() == 0) {
			return newName;
		} else {
			return getNewFieldName(tableName, newName + "_unused");
		}
	}

	private String getTableName(Class<?> clazz) {
		return StringUtil.isNotEmpty(clazz.getAnnotation(Table.class).name()) ? clazz.getAnnotation(Table.class).name()
				: clazz.getSimpleName().toLowerCase();
	}
}
