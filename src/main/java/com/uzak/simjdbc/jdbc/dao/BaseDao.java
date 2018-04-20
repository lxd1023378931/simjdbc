package com.uzak.simjdbc.jdbc.dao;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.uzak.simjdbc.exception.NullPrimaryKeyException;
import com.uzak.simjdbc.jdbc.annocation.Column;
import com.uzak.simjdbc.jdbc.annocation.Table;
import com.uzak.simjdbc.jdbc.pool.DataPool;
import com.uzak.simjdbc.jdbc.result.ResultList;
import com.uzak.simjdbc.jdbc.result.ResultRow;
import com.uzak.simjdbc.jdbc.transtion.Q;
import com.uzak.simjdbc.jdbc.transtion.SuperTranstion;
import com.uzak.simjdbc.util.StringUtil;

public class BaseDao extends SuperTranstion implements IBaseDao {

	Class<? extends BaseDao> clazz = null;
	Field[] fields = null;

	private void init() {
		clazz = this.getClass();
		if (!clazz.isAnnotationPresent(Table.class)) {
			throw new AnnotationFormatError("该实体类缺少@Table注解！");
		}
		fields = clazz.getDeclaredFields();
	}

	@Override
	public Object getValue(String columnName) {
		String method = "get" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
		try {
			Method getMethod = clazz.getMethod(method);
			return getMethod.invoke(this);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean insert() {
		return insert(DataPool.DEFAULT_POOLNAME);
	}

	@Override
	public boolean insert(String poolName) {
		return insertQ().executeInt(poolName) > 0 ? true : false;
	}

	@Override
	public Q insertQ() {
		init();
		String tableName = StringUtil.isEmpty(clazz.getAnnotation(Table.class).name()) ? clazz.getSimpleName().toLowerCase()
				: clazz.getAnnotation(Table.class).name();
		StringBuilder sql = new StringBuilder("insert into ").append(tableName).append(" (");
		StringBuilder valS = new StringBuilder("(");
		List<Object> paramList = new ArrayList<>();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			String columnName = StringUtil.isEmpty(column.name()) ? field.getName() : column.name();
			if (getValue(field.getName()) != null) {
				sql.append(columnName.toLowerCase() + ",");
				valS.append("?,");
				paramList.add(getValue(field.getName()));
			}
		}
		sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",") + 1, ") values ");
		valS.replace(valS.lastIndexOf(","), valS.lastIndexOf(",") + 1, ");");
		sql.append(valS);
		Q q = new Q(sql.toString(), paramList.toArray());
		return q;
	}

	@Override
	public boolean update() {
		return update(DataPool.DEFAULT_POOLNAME);
	}

	@Override
	public boolean update(String poolName) {
		return updateQ().executeInt(poolName) > 0 ? true : false;
	}

	@Override
	public Q updateQ() {
		init();
		String tableName = StringUtil.isEmpty(clazz.getAnnotation(Table.class).name()) ? clazz.getSimpleName().toLowerCase()
				: clazz.getAnnotation(Table.class).name();
		StringBuilder sql = new StringBuilder("update ").append(tableName).append(" set ");
		String whereSql = "";
		List<Object> paramList1 = new ArrayList<>();
		List<Object> paramList2 = new ArrayList<>();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			String columnName = StringUtil.isEmpty(column.name()) ? field.getName() : column.name();
			if (column.primary()) {
				if (getValue(field.getName()) == null) {
					try {
						throw new NullPrimaryKeyException("主键“" + columnName + "”为空！");
					} catch (NullPrimaryKeyException e) {
						e.printStackTrace();
					}
				} else {
					whereSql += " and " + columnName.toLowerCase() + "=? ";
					paramList2.add(getValue(field.getName()));
				}
			} else {
				if (getValue(field.getName()) != null) {
					sql.append(columnName.toLowerCase() + "=? , ");
					paramList1.add(getValue(field.getName()));
				} else {
					sql.append(columnName.toLowerCase() + "= null, ");
				}
			}
		}
		sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",") + 1, " where 1=1");
		paramList1.addAll(paramList2);
		Q q = new Q(sql.toString() + whereSql, paramList1.toArray());
		return q;
	}

	@Override
	public boolean delete() {
		return delete(DataPool.DEFAULT_POOLNAME);
	}

	@Override
	public boolean delete(String poolName) {
		return deleteQ().executeInt(poolName) > 0 ? true : false;
	}

	@Override
	public Q deleteQ() {
		init();
		String tableName = StringUtil.isEmpty(clazz.getAnnotation(Table.class).name()) ? clazz.getSimpleName().toLowerCase()
				: clazz.getAnnotation(Table.class).name();
		StringBuilder sql = new StringBuilder("delete from ").append(tableName).append(" where 1=1 ");
		List<Object> paramList = new ArrayList<>();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			String columnName = StringUtil.isEmpty(column.name()) ? field.getName() : column.name();
			if (column.primary()) {
				if (getValue(field.getName()) == null) {
					try {
						throw new NullPrimaryKeyException("主键“" + columnName + "”为空！");
					} catch (NullPrimaryKeyException e) {
						e.printStackTrace();
					}
				} else {
					sql.append(" and ").append(columnName.toLowerCase()).append("=? ");
					paramList.add(getValue(field.getName()));
				}
			}
		}
		Q q = new Q(sql.toString(), paramList.toArray());
		return q;
	}

	@Override
	public boolean fill() {
		return fill(DataPool.DEFAULT_POOLNAME);
	}

	@Override
	public boolean fill(String poolName) {
		init();
		boolean flag = false;
		try {
			String tableName = StringUtil.isEmpty(clazz.getAnnotation(Table.class).name()) ? clazz.getSimpleName().toLowerCase()
					: clazz.getAnnotation(Table.class).name();
			StringBuilder sql = new StringBuilder("select * from ").append(tableName).append(" where 1=1 ");
			List<Object> paramList = new ArrayList<>();
			for (Field field : fields) {
				Column column = field.getAnnotation(Column.class);
				String columnName = StringUtil.isEmpty(column.name()) ? field.getName() : column.name();
				if (column.primary()) {
					if (getValue(field.getName()) == null) {
						try {
							throw new NullPrimaryKeyException("主键“" + columnName + "”为空！");
						} catch (NullPrimaryKeyException e) {
							e.printStackTrace();
						}
					} else {
						sql.append(" and ").append(columnName.toLowerCase()).append("=? ");
						paramList.add(getValue(field.getName()));
					}
				}
			}
			Q q = new Q(sql.toString(), paramList.toArray());
			ResultList result = q.executeResultList(poolName);
			if (result.size() > 0) {
				ResultRow rr = result.get(0);
				if (setValue(rr)) {
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean setValue(ResultRow rr) {
		try {
			for (Field field : fields) {
				Class<?> type = field.getType();
				String columnName = field.getName();
				String method = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
				Method getMethod = clazz.getMethod(method, type);
				getMethod.invoke(this, rr.get(columnName.toLowerCase()));
			}
			return true;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}
}
