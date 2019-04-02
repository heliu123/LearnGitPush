package com.example.springBoot.util.secret;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class ThreeDes {
	/***
	 * 会话标识加密
	 * 
	 * @param src
	 * @return
	 */
	// 密钥
	private final static String secretKey = "78452EAAE8A86B904044Aeww";
	// CBC方式的初始化向量
	private final static byte[] iv = new byte[] { 93, 81, 122, 33, 47, 50, 17,
			103 };

	// 加解密统一使用的编码方式
	private final static String encoding = "utf-8";

	public static String encrypt(String src) {
		String ret = "";
		try {
			// 24字节密钥key,3倍DES密钥长度
			byte[] tripleKey = getKeyBytes(secretKey);
			// 生成密钥
			SecretKey secretKey = new SecretKeySpec(tripleKey, "DESede");
			String transformation = "DESede/CBC/PKCS5Padding";
			Cipher cipher = Cipher.getInstance(transformation);

			IvParameterSpec ivparam = new IvParameterSpec(iv);
			// 加密
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparam);
			byte[] encriptText = cipher.doFinal(src.getBytes(encoding));
			ret = bytesToHexString(encriptText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 3DES解密
	 * 
	 * @param encryptText
	 *            加密文本
	 * @return
	 * @throws Exception
	 */
	public static String decode(String encryptText) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		byte[] decryptData = cipher.doFinal(hexStringToBytes(encryptText));
		return new String(decryptData, encoding);
	}

	/** * 把字节数组转换成16进制字符串 * * @param bArray * @return */
	private static String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	private static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte[] getKeyBytes(String keyString) {
		byte[] b = new byte[24];
		byte[] key = keyString.getBytes();
		int count = keyString.getBytes().length;
		for (int i = 0; i < count; i++) {
			b[i] = key[i];
		}
		for (int i = count; i < 24; i++) {
			b[i] = 0x20;
		}
		return b;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

}
