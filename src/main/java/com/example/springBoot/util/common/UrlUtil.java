package com.wtyt.tsr.util.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * 关于url处理的类
 * @author Administrator
 *
 */
public class UrlUtil {
	
private static final String DEFAULT_CHARSETNAME = "utf-8";
	
	
	private static final int DEFAULT_CONNECTTIMEOUT=15000;//默认连接超时时间
	
	private static final int DEFAULT_READTIMEOUT=35000;//默认读取超时时间
	
	
	

	
	/**
	 * urlDecode一次
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static String urlDecode(String value, String charsetName) {
		if (StringUtil.isEmptyStr(value)) {
			return "";
		}
		if (StringUtil.isEmptyStr(charsetName)) {
			charsetName = DEFAULT_CHARSETNAME;
		}
		try {
			return java.net.URLDecoder.decode(value, charsetName);
		} catch (UnsupportedEncodingException e) {
			// 不处理
		}
		return "";
	}
	
	/**
	 * urlDecode一次
	 * @param value
	 * @return
	 */
	public static String urlDecode(String value) {
		return urlDecode(value, DEFAULT_CHARSETNAME);
	}
	
	/**
	 * urlDecode两次
	 * @param value
	 * @return
	 */
	public static String twoTimesDecode(String value) {
		return urlDecode(urlDecode(value));
	}
	
	/**
	 * urlEncode 一次
	 * @param value
	 * @return
	 */
	public static String urlEncode(String value) {
		return urlEncode(value, DEFAULT_CHARSETNAME);
	}
	
	
	/**
	 * urlEncode 两次
	 * @param value
	 * @param charsetName 编码格式
	 * @return
	 */
	public static String twoTimesEncode(String value, String charsetName) {
		return urlEncode(urlEncode(value, charsetName), charsetName);
	}
	
	/**
	 * urlEecode两次
	 * @param value
	 * @return
	 */
	public static String twoTimesEncode(String value) {
		return twoTimesEncode(value, DEFAULT_CHARSETNAME);
	}
	
	/**
	 * urlEncode 一次
	 * @param value
	 * @param charsetName 编码格式
	 * @return
	 */
	public static String urlEncode(String value, String charsetName) {
		try {
			return java.net.URLEncoder.encode(value, charsetName);
		} catch (UnsupportedEncodingException e) {
		}
		return "";
	}
	
	/**
	 * 执行post请求
	 * @param urlStr
	 * @param sendParams
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String urlStr, String sendParams)
			throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setConnectTimeout(EnvironmentUtil.getEnvValue("outer.connect.timeout", int.class, DEFAULT_CONNECTTIMEOUT));
		conn.setReadTimeout(EnvironmentUtil.getEnvValue("outer.read.timeout", int.class, DEFAULT_READTIMEOUT));
		OutputStream out = conn.getOutputStream();
		out.write(sendParams.getBytes("utf-8"));
		out.flush();
		out.close();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn
				.getInputStream(), "utf-8"));
		String line = null;
		StringBuffer buf = new StringBuffer();
		while ((line = br.readLine()) != null) {
			buf.append(line + "\n");
		}
		br.close();
		conn.disconnect();
		return buf.toString();
	}
	
	
	/**
	 * 执行post请求(url需要编码)
	 * @param urlStr
	 * @param sendParams
	 * @return
	 * @throws Exception
	 */
	public static String doPostEncode(String urlStr, String sendParams)	throws Exception {
		return twoTimesDecode(doPost(urlStr,twoTimesEncode(sendParams)));
	}
	
	/**
	 * 执行post请求(url需要编码),自定义超时时间
	 * @param urlStr
	 * @param sendParams
	 * @return
	 * @throws Exception
	 */
	public static String doPostEncode(String urlStr, String sendParams,int connectTimeOut,int readTimeOut)	throws Exception {
		return twoTimesDecode(doPost(urlStr,twoTimesEncode(sendParams),connectTimeOut,readTimeOut));
	}
	
	/**
	 * 执行post请求
	 * @param urlStr
	 * @param sendParams
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String urlStr, String sendParams,int connectTimeOut,int readTimeOut)
			throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setConnectTimeout(connectTimeOut);
		conn.setReadTimeout(readTimeOut);
		OutputStream out = conn.getOutputStream();
		out.write(sendParams.getBytes("utf-8"));
		out.flush();
		out.close();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn
				.getInputStream(), "utf-8"));
		String line = null;
		StringBuffer buf = new StringBuffer();
		while ((line = br.readLine()) != null) {
			buf.append(line + "\n");
		}
		br.close();
		conn.disconnect();
		return buf.toString();
	}
	
}
