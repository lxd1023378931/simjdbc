package com.uzak.simjdbc.jdbc.annocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.uzak.simjdbc.jdbc.autobuild.ColumnType;

/**
 * 映射表字段
 * 
 * @author 梁秀斗
 * @mail 1023378931@qq.com
 * @date 2018年4月14日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Column {
	/**
	 * 字段名
	 * 
	 * @return
	 */
	public String name() default "";

	/**
	 * 字段类型
	 * 
	 * @return
	 */
	public ColumnType type() default ColumnType.NULL;

	/**
	 * 字段长度
	 * 
	 * @return
	 */
	public int length() default 255;

	/**
	 * 是否为主键
	 * 
	 * @return
	 */
	public boolean primary() default false;

	/**
	 * 是否不为空
	 * 
	 * @return
	 */
	public boolean notnull() default false;

	/**
	 * 不允许重复
	 * 
	 * @return
	 */
	public boolean unique() default false;

}
