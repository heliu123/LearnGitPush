package com.wtyt.tsr.util.common;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import com.wtyt.tsr.util.exception.BaseException;
import com.wtyt.tsr.util.secret.AESCoder;






/** 
 * 关于字符串处理的类
 * @author Administrator
 *
 */
public class StringUtil {
	
	/**图片正则*/
	public  static  final  Pattern PATTERN = Pattern.compile("<\\s*(img|IMG)\\s+([^>]+)\\s*>",Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
	
	/**html正则*/
	public  static  final  Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");
	
	/**驼峰的正则*/
	private static Pattern humpPattern = Pattern.compile("[A-Z]");  
	
	/**下划线的正则*/
	private static Pattern linePattern = Pattern.compile("_(\\w)");  
	
	private static Logger log = LogManager.getLogger(StringUtil.class);
	
	/**
	 * 将整形的字符串转换成整形
	 * @param str
	 */
	@SuppressWarnings("serial")
	public static  Number getEmptyNum(final String str){
		if(StringUtils.isEmpty(str)){
			return 0;
		}
		Number num = 0;
		try{				
			num = new Number() {					
				public long longValue() {					
					return Long.parseLong(str);
				}
				public int intValue() {				
					return Integer.parseInt(str);
				}
				public float floatValue() {					
					return Float.parseFloat(str);
				}
				public double doubleValue() {
					return Double.parseDouble(str);
				}
			};			
		}catch(NumberFormatException e){			
			throw new RuntimeException(e);		
		}	
		return num;
	}  

	
	/**
	 * 通过json获取到value的值
	 * @param paramObj
	 * @param keyName
	 * @return
	 * @throws ServletException
	 */
	public static String getJsonObjectValue(JSONObject paramObj,String keyName) throws BaseException{
		String value = "";		
		try {
			value = paramObj.getString(keyName);
		} catch (JSONException e) {
			throw new BaseException("获取不到"+keyName+"的值");
		}	
		if(StringUtils.isEmpty(value)){
			throw new BaseException("获取不到"+keyName+"的值");
		}
		return value;
	}
	
	
	/**
	 * 通过json获取到value的值,可以是空值
	 * @param paramObj
	 * @param keyName
	 * @return
	 * @throws ServletException
	 */
	public static String getJsonObjectValueCanNull(JSONObject paramObj,String keyName) throws BaseException{
		String value = "";		
		try {
			value = paramObj.getString(keyName);
		} catch (Exception e) {
			log.info("获取不到"+keyName+"的值");
			value = "";
		}			
		return value;
	}
	
	
	public static String getStringNoBlank(String str) {      
        if(str!=null && !"".equals(str)) {      
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");      
            Matcher m = p.matcher(str);      
            String strNoBlank = m.replaceAll("");      
            return strNoBlank;      
        }else {      
            return str;      
        }           
    } 
	
	
	
		
	
	
	/**
	 * 获取解密后的value
	 * @param value
	 * @return
	 */
	public static String getDecodeNumStr(String value) {
		if (StringUtils.isEmpty(value)) {
			return "";
		}
		if (isNumStr(value)) {
			return value;
		} else {
			return getDecodeNumStr(AESCoder.doDecodeStr(value));
		}

	}
	
	/**
	 * 获取过滤字符串，前后空格和null
	 * @param value
	 * @return
	 */
	public static String getFliterNullStr(String value) {
		if (StringUtils.isEmpty(value)) {
			return "";
		}
		return value.trim();
	}
	
	/**
	 * 判断字符串是否是数字
	 * @param value
	 * @return
	 */
	public static boolean  isNumStr(String value){
		if (null==value) {
			return false;
		}
		return value.matches("(\\+|-)?\\d*");
	}
	
	/**
	
	
	/**
	 * 判断容器是否包含
	 * @param origin
	 * @param target
	 * @return
	 */
    public static <T> boolean  isCollectionContains(T t,Collection<T> coll){
    	if(coll==null||coll.size()==0){
    		return false;
    	}
    	if(t==null||isEmptyStr(String.valueOf(t))){
    		return false;
    	}    	
    	return coll.contains(t);
    }
    
 
	  

   /**
	 * 去除内容的表情以及图片的相关信息
	 * @param content
	 * @return
	 */
	public static String removeImgRegix(String content){
		if(isEmptyStr(content)){
			return content;
		}	
		Matcher matcher = PATTERN.matcher(content);	   	
		if(matcher.find()){
			String[] results  = PATTERN.split(content);	
			StringBuffer contentSb = new StringBuffer();
		    for(String cont:results){	
		    	contentSb.append(cont);		    	
		    }
		    content = contentSb.toString();
		}
		return content;
	}
	
	/**
	 * 去除内容的html标签
	 * @param content
	 * @return
	 */
	public static String removeHtmlRegix(String content){
		if(isEmptyStr(content)){
			return content;
		}	
		Matcher matcher = HTML_PATTERN.matcher(content);	   	
		if(matcher.find()){
			String[] results  = HTML_PATTERN.split(content);	
			StringBuffer contentSb = new StringBuffer();
		    for(String cont:results){	
		    	contentSb.append(cont);		    	
		    }
		    content = contentSb.toString();
		}
		return content;
	}
	
	/**
	 * 获取固定长度的字符串
	 * @param content
	 * @param length
	 * @return
	 */
	public static String getContentLongLength(String content,int length){
		  if(isEmptyStr(content)){
			  return content;
		  }
		  if(content.length()>=length){
			  content = content.substring(0, length);
		  }
		  return content;
	}
	
	/**
	 * 获取固定长度的字符串
	 * @param content
	 * @param length
	 * @return
	 */
	public static String getContentLongLengthTail(String content,int length){
		  if(isEmptyStr(content)){
			  return content;
		  }
		  if(content.length()>length){
			  content = content.substring(0, length)+"...";
		  }
		  return content;
	}
	
	/**
	 * 判断字符串是否为空
	 * 
	 * @param seq
	 * @return
	 */
	public static boolean isEmptyStr(String str) {
		return null == str || "".equals(str.trim()) || "null".equals(str);
	}
	
	public static boolean isEmptyStr(Object str) {
		return null == str || "".equals(str) || "null".equals(str);
	}
	

	public static String camelToUnderline(String param){  
	       if (param==null||"".equals(param.trim())){  
	           return "";  
	       }  
	       int len=param.length();  
	       StringBuilder sb=new StringBuilder(len);  
	       for (int i = 0; i < len; i++) {  
	           char c=param.charAt(i);  
	           if (Character.isUpperCase(c)){  
	               sb.append('_');  
	               sb.append(Character.toLowerCase(c));  
	           }else{  
	               sb.append(c);  
	           }  
	       }  
	       return sb.toString();  
	   }  
	   public static String underlineToCamel(String param){  
	       if (param==null||"".equals(param.trim())){  
	           return "";  
	       }  
	       int len=param.length();  
	       StringBuilder sb=new StringBuilder(len);  
	       for (int i = 0; i < len; i++) {  
	           char c=param.charAt(i);  
	           if (c=='_'){  
	              if (++i<len){  
	                  sb.append(Character.toUpperCase(param.charAt(i)));  
	              }  
	           }else{  
	               sb.append(c);  
	           }  
	       }  
	       return sb.toString();  
	   }  
	   
	   
       /**下划线转驼峰*/  
       public static String lineToHump(String str){  
           str = str.toLowerCase();  
           Matcher matcher = linePattern.matcher(str);  
           StringBuffer sb = new StringBuffer();  
           while(matcher.find()){  
               matcher.appendReplacement(sb, matcher.group(1).toUpperCase());  
           }  
           matcher.appendTail(sb);  
           return sb.toString();  
       }  
      
       /**驼峰转下划线,效率比上面高*/  
       public static String humpToLine(String str){  
           Matcher matcher = humpPattern.matcher(str);  
           StringBuffer sb = new StringBuffer();  
           while(matcher.find()){  
               matcher.appendReplacement(sb, "_"+matcher.group(0).toLowerCase());  
           }  
           matcher.appendTail(sb);  
           return sb.toString();  
       }   
	   
       /**
        * 将null类型的字符串转换成空字符串
        * @param value
        * @return
        */
       public static String getEmptyStr(String value){
    	   return null==value?"":value;
       }
       

   /**
    * 判断字符串是否为空,当字符串为null、空串、空格组成时,返回true,其他返回false
    * @author pangShikui
    * @date Oct 24, 2018
    * @param str
    * @return
    */
	public static boolean isEmpty(String str){
		if (null==str || "".equals(str) || "null".equals(str) || str.length()==0 || str.replaceAll(" ", "").length()==0 ) {
			return true;
		}
		return false;
	}
}


