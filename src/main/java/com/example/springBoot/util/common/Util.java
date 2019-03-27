package com.wtyt.tsr.util.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.wtyt.tsr.util.init.StartUp;
import com.wtyt.tsr.util.trace.LogTracableProxyHandler;
import com.wtyt.tsr.util.trace.LogTraceContext;



public class Util {
	
	
	
	
	/**
	 * 将iterable的内容拼接出来
	 * @param t
	 * @param splitStr
	 * @return
	 */
	public static <T extends Iterable<?>>  String   getIterableStr(T  t,String splitStr){
		Iterator<?>  it = t.iterator();
		StringBuilder sb = new StringBuilder();
		while(it.hasNext()){
			sb.append(it.next()).append(splitStr);
		}
		String returnStr = sb.toString();
		if(returnStr.endsWith(splitStr)){
			returnStr = returnStr.substring(0, returnStr.length()-splitStr.length());
		}
		return returnStr;		
	}
	
	/**
	 * 将array转换成list(默认转化成arrayList)
	 * @param array
	 * @return
	 */
	public static <T>  List<T>  changeArrayToList(T[] array){
		List<T>  arrayList = new ArrayList<T>();
		if(array!=null&&array.length>0){
			 for(T t:array){
				 arrayList.add(t);
			 }
		}		
		return arrayList;
	}
	
	/**
	 * 将字符串第一个字母小写
	 * @param name
	 * @return
	 */
	public static String captureLower(String name) {
        char[] cs=name.toCharArray();
        cs[0]+=32;
        return String.valueOf(cs);
         
    }
	
	
    /**
     * 将list转换成set
     * @param selectNickNameSensitiveWords
     * @return
     */
	public static <T> Set<T> changeListToSet(List<T> selectNickNameSensitiveWords) {
		Set<T>  set = new HashSet<T>();
		if(null!=selectNickNameSensitiveWords){
			for(T t:selectNickNameSensitiveWords){
				set.add(t);
			}
		}
		return set;
	}
	
	

	/**
	 * 获取异常信息的具体内容
	 * @param t
	 * @return
	 * @throws IOException 
	 */
	public static String getExceptionStr(Throwable t) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 	
		try {
			t.printStackTrace(new PrintStream(baos)); 
			return baos.toString(); 
		}finally{			
			try {
				baos.close();
			} catch (Exception e) {
			}
		}
				
	}
	
	/**
	 * 将集合转换成数组
	 * @param coll
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  static <T> T[]  collToArray(Collection<T> coll,Class<T>  clazz){
		if(coll==null||coll.isEmpty()){
			return null;
		}
		T[]  tArray = (T[]) Array.newInstance(clazz, coll.size());
		Iterator<T>  it = coll.iterator();
		int index=0;
		while(it.hasNext()){
			tArray[index++]=it.next();
		}		
		return tArray;	
	}
	
	/**
	 * 当前线程睡眠多少毫秒数
	 * @param millis
	 */
	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 线程池加入thread Log
	 * @param t
	 */
	public static <T extends  Runnable>	 void threadPoolTraceLog(T  t){
    	Object  object =  Proxy.newProxyInstance(t.getClass().getClassLoader(),t.getClass().getInterfaces(),new LogTracableProxyHandler(t,LogTraceContext.getTraceDetail()));
		if(object  instanceof Runnable){
			Runnable  runnable = (Runnable) object;
			StartUp.executorService.execute(runnable);
		}
	}
	
	//操作系统判断
	public static boolean isWindowOs(){
		String os = System.getProperty("os.name").toLowerCase();  
		return os.indexOf("windows")>=0;  
	}
	
	/**
	 * 中文转码
	 * 
	 * @param str
	 * @return String
	 * @see java.net.URLEncoder.encode(String s, String enc)
	 */
	public static String URLEncode2UTF8(String str) {
		if (null == str)
			return null;
		String strRet = "";
		try {
			strRet = java.net.URLEncoder.encode(str, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}

	public static String URLEncode2UTF8TwoTimes(String str) {
		return URLEncode2UTF8(URLEncode2UTF8(str));
	}
	
	public static String URLDecode2UTF8TwoTimes(String str) {
		return URLDecode2UTF8(URLDecode2UTF8(str));
	}

	public static String URLDecode2UTF8(String str) {
		if (null == str)
			return null;
		String strRet = "";
		try {
			strRet = java.net.URLDecoder.decode(str, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}
	
	
	/**
	 * 
	 * 获取用户的ip地址
	 * 
	 * @param request
	 * @return 用户ip
	 */
	public static String getRemortIP(HttpServletRequest request) {
		String result = request.getHeader("x-forwarded-for");
		if (result != null && result.trim().length() > 0) {
			// 可能有代理
			if (result.indexOf(".") == -1) {
				// 没有"."肯定是非IPv4格式
				result = null;
			} else {
				if (result.indexOf(",") != -1) {
					// 有","，估计多个代理。取第一个不是内网的IP。
					result = result.trim().replace("'", "");
					String[] temparyip = result.split(",");
					for (int i = 0; i < temparyip.length; i++) {
						if (isIPAddress(temparyip[i])
								&& temparyip[i].substring(0, 3).equals("10.")
								&& temparyip[i].substring(0, 7).equals(
										"192.168")
								&& temparyip[i].substring(0, 7).equals(
										"172.16.")) {
							return temparyip[i]; // 找到不是内网的地址
						}
					}
				} else if (isIPAddress(result)) {// 代理即是IP格式
					return result;
				} else {
					result = null; // 代理中的内容 非IP，取IP
				}
			}
		}

		if (result == null || result.trim().length() == 0) {
			result = request.getHeader("Proxy-Client-IP");
		}
		if (result == null || result.length() == 0) {
			result = request.getHeader("WL-Proxy-Client-IP");
		}
		if (result == null || result.trim().length() == 0) {
			result = request.getRemoteAddr();
		}
		return result;
	}

	/**
	 * 判断是否是IP地址格式
	 * 
	 * @param str1
	 * @return
	 */
	private static boolean isIPAddress(String str1) {
		if (str1 == null || str1.trim().length() < 7
				|| str1.trim().length() > 15) {
			return false;
		}
		return true;
	}
}
