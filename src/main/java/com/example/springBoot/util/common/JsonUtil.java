package com.example.springBoot.util.common;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JsonUtil {
	private static Logger logger = LogManager.getLogger(JsonUtil.class);
	
	private static  String NOT_JSON = "0";//不是jsonObject  也不是jsonArray
	private static  String JSON_ONLY_OBJ = "1";//是jsonObject
	private static  String JSON_ONLY_ARRAY = "2";//是jsonArray


	/**
	 * 将Object对象转换成JSON格式的字符串返回
	 * @throws JsonProcessingException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static String objectToJson(Object obj) throws JsonProcessingException  {
		ObjectMapper objectMapper = new ObjectMapper();		
		return objectMapper.writeValueAsString(obj);
	}
	
	/**
	 * 将Object对象转换成JSON格式的字符串返回
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static String objectToJson(Object obj,PropertyNamingStrategy strategy) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(strategy);
		return objectMapper.writeValueAsString(obj);
	}

	/**
	 * 将Object对象转换成JSON格式的字符串返回
	 * Object对象null值处理为空串
	 * 集合类属性如果为null，也会映射为空串，使用时候需要注意到
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public static String objectToJsonNullToBlank(Object obj,PropertyNamingStrategy strategy) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		// 空值处理为空串
		objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
				jg.writeString("");
			}
		});

		objectMapper.setPropertyNamingStrategy(strategy);
		return objectMapper.writeValueAsString(obj);
	}
	
		
	/**
	 * 将Object对象转换成JSON格式的字符串返回（忽略空值）
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static String objectToJsonNotNull(Object obj,PropertyNamingStrategy strategy) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(strategy);
		objectMapper.setSerializationInclusion(Include.NON_NULL);  
		return objectMapper.writeValueAsString(obj);
	}
	
	

	
	
	/**
	 * 获取json里面某个内容的值（可空）
	 * @param json
	 * @param fieldName
	 * @return
	 */
	public static String getJsonFieldValueCanNull(JSONObject json,String fieldName){
		String returnStr = "";
		try{
			returnStr = json.get(fieldName).toString();
		}catch(JSONException e){
			returnStr = "";
			logger.info("warning!!获取不到"+fieldName+"的值");
		}
		return returnStr;
	}
	
	/**
	 * 获取json里面某个内容的值（不可空）
	 * @param json
	 * @param fieldName
	 * @return
	 * @throws JSONException 
	 */
	public static String getJsonFieldValue(JSONObject json,String fieldName) throws JSONException{
		String returnStr = "";
		try{
			returnStr = json.get(fieldName).toString();
			if("".equals(returnStr.trim())){
				throw new JSONException(fieldName+"的值不允许为空字符串~");
			}
		}catch(JSONException e){
			//e.printStackTrace();
			throw new JSONException(fieldName+"的值不允许为空！");
		}
		return returnStr;
	}

	/**
	 * 将Object对象转换成JSON格式的字符串返回
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static String objectToJsonNotNull(Object obj) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);  
		return mapper.writeValueAsString(obj);
	}
	
	/**
	 * 将Object对象转换成JSON格式的字符串返回
	 * bean中的驼峰属性转化成json里面的下划线key
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static String objectToJsonNotNullForKebabCase(Object obj) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		mapper.setSerializationInclusion(Include.NON_NULL);  
		return mapper.writeValueAsString(obj);
	}
	
	/**
	 * 将json转换成object
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static <T> T  jsonToObject(String  jsonStr,Class<T> clazz) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		return (T) objectMapper.readValue(jsonStr, clazz);
	}
	
	
	/**
	 * 判断当前的objec的json类型
	 * @param object
	 * @return  0代表纯字符串  1代表json字符串  2代表jsonArray的字符串
	 */
	private static String  getJsonType(Object  object){
		 String objectStr = String.valueOf(object);
		 if(StringUtils.isEmpty(objectStr)){
			 return NOT_JSON;
		 }		 
		 final char[] strChar = objectStr.substring(0, 1).toCharArray();
		 final char firstChar = strChar[0];
		 if(firstChar == '{'){
			 return JSON_ONLY_OBJ;
		 }else if(firstChar == '['){
			 return JSON_ONLY_ARRAY;
		}
		 return NOT_JSON;
	}

	/**
	 * 判断内省是否为json
	 * @param string
	 * @return
	 */
	public static boolean isJsonObject(String object) {		
		return JSON_ONLY_OBJ.equals(getJsonType(object));
	}
	
	
	/**
	 * 获取访问公共项目接口的签名json
	 * @param typeCode
	 * @return
	 * @throws JSONException 
	 */
	public  static JSONObject getVisitCommPatSignJson(String typeCode) throws JSONException{
		JSONObject  signJson = new JSONObject();
		String timeStamp =String.valueOf(new java.util.Date().getTime());
		signJson.put("type_code", typeCode);
		signJson.put("time_stamp", timeStamp);
		//signJson.put("check_value", MD5Coder.md5(typeCode+timeStamp+System.getProperty("LB_SQ_KEY")));
		return signJson;
	}
}
