package com.uzak.simjdbc.jdbc.dao;

import com.uzak.simjdbc.jdbc.Q;
import com.uzak.simjdbc.jdbc.ResultRow;

/**
 * 实体类的基本数据操作
 * 
 * @author 梁秀斗
 * @mail 1023378931@qq.com
 * @date 2018年4月14日
 */
public interface IBaseDao {

	Object getValue(String columnName);

	public boolean insert();

	public boolean insert(String poolName);

	public Q insertQ();

	public boolean update();

	public boolean update(String poolName);

	public Q updateQ();

	public boolean delete();

	public boolean delete(String poolName);

	public Q deleteQ();

	public boolean fill();

	public boolean fill(String poolName);

	public boolean setValue(ResultRow rr);
}
