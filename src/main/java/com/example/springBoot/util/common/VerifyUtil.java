package com.example.springBoot.util.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtil {
	static String CmccNoPattern = System.getProperty("CmccNoPattern"); // 移动
	static String UnicomNoPattern = System.getProperty("UnicomNoPattern"); // 联通
	static String TelecomNoPattern = System.getProperty("TelecomNoPattern"); // 电信
	static String MobileNo = System.getProperty("MobileNoPattern");

	// 是否是移动手机
	public static boolean isCmccNoPatternMobile(String str) {
		Pattern p = Pattern.compile(CmccNoPattern);
		Matcher m = p.matcher(str);
		boolean b = m.matches();
		return b;
	}

	// 是否是联通手机
	public static boolean isUnicomNoPatternMobile(String str) {
		Pattern p = Pattern.compile(UnicomNoPattern);
		Matcher m = p.matcher(str);
		boolean b = m.matches();
		return b;
	}

	// 是否是电信手机
	public static boolean isTelecomNoPatternMobile(String str) {
		Pattern p = Pattern.compile(TelecomNoPattern);
		Matcher m = p.matcher(str);
		boolean b = m.matches();
		return b;
	}
	
	/**
	 * 判断是否为手机号码
	 * @param mobileNo
	 *            String
	 * @return boolean
	 */
	public static boolean isMobileNo(String mobileNo) {
		Pattern p = Pattern.compile(MobileNo);
		Matcher m = p.matcher(mobileNo);
		return m.matches();
	}
}
