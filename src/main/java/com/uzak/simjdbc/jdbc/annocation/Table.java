package com.uzak.simjdbc.jdbc.annocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 映射数据库表
 * 
 * @author 梁秀斗
 * @mail 1023378931@qq.com
 * @date 2018年4月14日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Table {
	String name() default "";
}
