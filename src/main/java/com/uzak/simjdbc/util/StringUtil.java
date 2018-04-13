package com.uzak.simjdbc.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * String 操作工具类
 * 
 * @author 梁秀斗
 * @mail liangxiudou@zving.com
 * @date 2018年3月29日
 */
public class StringUtil {
	public static final Pattern PDigit = Pattern.compile("^\\d+(\\.\\d+)*$");
	public static char HexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 判断对象是否为字符串类型
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isString(Object obj) {
		return obj instanceof String;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		}
		return str.trim().length() == 0 ? true : false;
	}

	/**
	 * 判断字符串是否不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 判断是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDigit(String str) {
		return isEmpty(str) ? false : PDigit.matcher(str).find();
	}

	/**
	 * MD5加密
	 * 
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String md5Hex(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		return hexEncode(md5.digest(str.getBytes("UTF-8")));
	}

	public static String hexEncode(byte[] data) {
		int l = data.length;
		char[] out = new char[l << 1];
		int i = 0;

		for (int j = 0; i < l; ++i) {
			// 高4位
			out[j++] = HexDigits[(240 & data[i]) >>> 4];
			// 低4位
			out[j++] = HexDigits[15 & data[i]];
		}

		return new String(out);
	}
}
