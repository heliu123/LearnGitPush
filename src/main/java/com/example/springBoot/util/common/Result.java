package com.example.springBoot.util.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * 获取各种返回结果的信息
 * 
 * @author 御饕餮
 * @date 2018年3月2日 下午3:38:28
 */
public class Result {

	private static final Logger log = LogManager.getLogger(Result.class);

	/**
	 * 获取返回结果
	 * 
	 * @return
	 * @throws JSONException
	 */
	public static String getResult(String req_code, String req_msg) {
		JSONObject returnJson = new JSONObject();
		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("reCode", req_code);
			resultJson.put("reInfo", req_msg);
			returnJson.put("result", resultJson);
		} catch (JSONException e) {
		//	throw new BaseRtException(CodeConstants.SYS_ERROR_CODE, "垃圾第三方json组件发生异常！！！", e);
		}
		String returnStr = returnJson.toString();
		log.debug("接口返回结果为：" + returnStr);
		return returnStr;
	}

	

	/**
	 * 获取返回结果
	 * 
	 * @return
	 * @throws JSONException
	 */
	private static JSONObject getJSONResult(String req_code, String req_msg) {
		JSONObject returnJson = new JSONObject();
		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("reCode", req_code);
			resultJson.put("reInfo", req_msg);
			returnJson.put("result", resultJson);
		} catch (JSONException e) {
		//	throw new BaseRtException(CodeConstants.SYS_ERROR_CODE, "垃圾第三方json组件发生异常！！！", e);
		}
		return returnJson;
	}

	/**
	 * 获取返回的成功结果
	 * 
	 * @return
	 */
/*	public static String getResult() {
		return getResult(CodeConstants.SUCCESS_CODE, CodeConstants.SUCCESS_MSG);
	}*/

	/**
	 * 获取返回的成功结果
	 * 
	 * @return
	 */
/*	public static String getResult(Object obj) {
		try {
			JSONObject returnJson = getJSONResult(CodeConstants.SUCCESS_CODE, CodeConstants.SUCCESS_MSG);
			if(obj != null){
				String dataJson = null;
				if (obj instanceof String) {
					dataJson = String.valueOf(obj);
				} else {
					dataJson = JsonUtil.objectToJson(obj, PropertyNamingStrategy.SNAKE_CASE);
				}
				returnJson.put("data", new JSONObject(dataJson));
			}
			String returnStr = returnJson.toString();
			log.debug("接口返回结果为：" + returnStr);
			return returnStr;
		} catch (Exception e) {
			throw new BaseRtException(CodeConstants.SYS_ERROR_CODE, "json组件发生异常！！！", e);
		}

	}*/
	
/*	*//**
	 * 获取返回的成功结果
	 * null值转化为空串
	 * SNAKE_CASE转换
	 * @return
	 *//*
	public static String getResultNullToBlank(Object obj) {
		try {
			JSONObject returnJson = getJSONResult(CodeConstants.SUCCESS_CODE, CodeConstants.SUCCESS_MSG);
			if(obj != null){
				String dataJson = null;
				if (obj instanceof String) {
					dataJson = String.valueOf(obj);
				} else {
					dataJson = JsonUtil.objectToJsonNullToBlank(obj, PropertyNamingStrategy.SNAKE_CASE);
				}
				returnJson.put("data", new JSONObject(dataJson));
			}
			String returnStr = returnJson.toString();
			log.debug("接口返回结果为：" + returnStr);
			return returnStr;
		} catch (Exception e) {
			throw new BaseRtException(CodeConstants.SYS_ERROR_CODE, "json组件发生异常！！！", e);
		}

	}

	*//**
	 * 获取返回的成功结果
	 * 忽略null
	 * SNAKE_CASE转换
	 * @return
	 *//*
	public static String getResultExcludeNull(Object obj) {
		try {
			JSONObject returnJson = getJSONResult(CodeConstants.SUCCESS_CODE, CodeConstants.SUCCESS_MSG);
			if(obj != null){
				String dataJson = null;
				if (obj instanceof String) {
					dataJson = String.valueOf(obj);
				} else {
					dataJson = JsonUtil.objectToJsonNotNull(obj, PropertyNamingStrategy.SNAKE_CASE);
				}
				returnJson.put("data", new JSONObject(dataJson));
			}
			String returnStr = returnJson.toString();
			log.debug("接口返回结果为：" + returnStr);
			return returnStr;
		} catch (Exception e) {
			throw new BaseRtException(CodeConstants.SYS_ERROR_CODE, "json组件发生异常！！！", e);
		}

	}
	
	

	*//**
	 * 获取返回的自定义成功结果
	 * 
	 * @return
	 *//*
	public static String getResult(String dataStr, Object obj) {
		try {
			String dataJson = null;
			if (obj instanceof String) {
				dataJson = String.valueOf(obj);
			} else {
				dataJson = JsonUtil.objectToJson(obj, PropertyNamingStrategy.SNAKE_CASE);
			}
			JSONObject returnJson = getJSONResult(CodeConstants.SUCCESS_CODE, CodeConstants.SUCCESS_MSG);
			if (StringUtil.isEmptyStr(dataStr)) {
				returnJson.put(dataStr, new JSONObject(dataJson));
			}
			return returnJson.toString();
		} catch (Exception e) {
			throw new BaseRtException(CodeConstants.SYS_ERROR_CODE, "json组件发生异常！！！", e);
		}

	}*/

}
