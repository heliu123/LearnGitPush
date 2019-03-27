package com.wtyt.tsr.util.common;



/**
 * 用户处理app客户端特殊交互
 * @author JML
 *
 */
public class AppUtil {

	private final static String ANDROID_KEY = "kydd_android"; //androidapp特有标识key
	private final static String IOS_KEY = "kydd_ios";//iosapp特有标识key
	private final static String WX_KEY = "micromessenger";//微信app特有标识key
	private static AppUtil appUtil = null;
	public static AppUtil getInstance(){
		if(appUtil==null){
			  synchronized (AppUtil.class){  
	                if(appUtil == null){  
	                	appUtil = new AppUtil();
	                }  
	            }  
		}
		return appUtil;
	}
	
	public static boolean isEmpty(String s){
		if(s==null || s.length()==0){return true;}
		return false;
	}
	/**
	 * 是否是androidapp
	 * @param userAgent
	 * @return
	 */
	public static boolean isAndroidApp(String userAgent){
		if(!isEmpty(userAgent) && userAgent.toLowerCase().contains(ANDROID_KEY)){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 是否是iosapp
	 * @param userAgent
	 * @return
	 */
	public static boolean isIosApp(String userAgent){
		if(!isEmpty(userAgent) && userAgent.toLowerCase().contains(IOS_KEY)){
			return true;
		}
		return false;
	}
	/**
	 * 是否是app
	 */
	public static boolean isKyApp(String userAgent){
		if(isIosApp(userAgent) || isAndroidApp(userAgent)){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否是weixinapp
	 * @param userAgent
	 * @return
	 */
	public static boolean isWeixinApp(String userAgent){
		if(!isEmpty(userAgent) && userAgent.toLowerCase().contains(WX_KEY)){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否是window系统
	 * @return
	 */
	public static boolean isWindowOs(){
		try{
			String os = System.getProperty("os.name");
			if (os.toLowerCase().indexOf("windows") >= 0) {
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
}
