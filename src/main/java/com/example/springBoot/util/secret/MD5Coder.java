package com.example.springBoot.util.secret;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Coder {
	
	/**
	 * md5����
	 * @param password
	 * @return
	 */
	public static String md5(String password){
		if(password==null||"".equals(password)){
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(password.getBytes());
		    return byte2hex(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String sign(String content, String charset, String key){
	    try{
	    	if (key == null) {
	    		throw new Exception("签名key为空!");
	    	}
	    	String signStr = String.format("%s%s", new Object[] { content, key });
	    	return md5(signStr, charset);
	    } catch (Exception e) {
	    	// throw new Exception("生成签名出错:" + e.getMessage(), e);
	    }
	    return key;
	}
	
	/**
	 * md5����
	 * @param password
	 * @return
	 */
	public static String md5(String password,String encoding){
		if(password==null||"".equals(password)){
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageByte = password.getBytes(encoding);  
		    byte[] md5Byte = md.digest(messageByte);   
		    return byte2hex(md5Byte);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected static  String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

}
