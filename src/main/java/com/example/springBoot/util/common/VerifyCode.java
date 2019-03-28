package com.example.springBoot.util.common;

import java.util.Random;

public class VerifyCode {
	private static final int VALIDATE_CODE_LENGTH = 6;// 短信验证码长度
	/*
	 * 生成6位数字验证码
	 */
	public static String generValidateCode() {
		char[] c = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		Random random = new Random(); // 初始化随机数产生器
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < VALIDATE_CODE_LENGTH; i++) {
			sb.append(c[Math.abs(random.nextInt()) % c.length]);
		}
		return sb.toString();
	}
}
