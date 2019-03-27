package com.wtyt.tsr.util.common;

import java.util.Enumeration;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.wtyt.lgms.util.LgmsUtil;
import com.wtyt.tsr.util.bean.JedisSessionBean;
import com.wtyt.tsr.util.bean.UserInfoBean;
import com.wtyt.tsr.util.constants.CodeConstants;
import com.wtyt.tsr.util.constants.LgmsConstants;
import com.wtyt.tsr.util.exception.BaseException;
import com.wtyt.tsr.util.exception.BaseRtException;
import com.wtyt.tsr.util.jedis.session.JedisSessionHandle;
import com.wtyt.tsr.util.secret.AESCoder;
import com.wtyt.tsr.util.secret.ThreeDes;

public class UserUtil {
	
	private static Logger log = LogManager.getLogger(UserUtil.class);
	
	private final static String CLIENT = "/client/";//APP客户端访问
	private final static String HTML = "/html/";//H5访问

	/**
	 * 从cookie里面获取token
	 * @param request
	 * @return
	 */
	public static String getTokenFromCookie(HttpServletRequest request){
		Cookie[]  cookies = request.getCookies();	
		if(null!=cookies){
			for(Cookie  cookie:cookies){
				log.info("=>阿金测试:"+cookie.getName()+","+cookie.getValue());
				if("token".equals(cookie.getName())){
					return cookie.getValue();
				}
			}
		}		
		return  null;
	}

	/**
	 * 获取接口请求的用户信息（用户信息必须存在）
	 * @param clientName
	 * @param header
	 * @param suid
	 * @return
	 * @throws Exception 
	 */
	public static String getReqUserIdNn(String token,String suid) throws Exception {
		if(!StringUtil.isEmptyStr(suid)){
			return AESCoder.doDecodeStr(suid);
		}
		if(!StringUtil.isEmptyStr(token)){
			return AESCoder.doDecodeStr(ThreeDes.decode(token));
		}		
		throw new BaseException(CodeConstants.TOKEN_ERROR_CODE,"当前接口不允许用户未登录");
	}
	
	/**
	 * 获取接口请求的用户信息（用户信息必须存在）
	 * @param clientName
	 * @param header
	 * @param suid
	 * @return
	 * @throws Exception 
	 */
	public static String getReqUserIdNn(HttpServletRequest request,String suid) throws Exception {
		if(!StringUtil.isEmptyStr(suid)){
			return AESCoder.doDecodeStr(suid);
		}
		String token = getTokenFromCookie(request);
		if(!StringUtil.isEmptyStr(token)){
			return AESCoder.doDecodeStr(ThreeDes.decode(token));
		}		
		throw new BaseException(CodeConstants.TOKEN_ERROR_CODE,"当前接口不允许用户未登录");
	}
	
	/**
	 * 获取接口请求的用户信息（用户信息不是必须存在）
	 * @param clientName
	 * @param header
	 * @param suid
	 * @return
	 * @throws Exception 
	 */
	public static String getReqUserIdCanNull(HttpServletRequest request,String suid) throws Exception {
		if(!StringUtil.isEmptyStr(suid)){
			return AESCoder.doDecodeStr(suid);
		}
		String token = getTokenFromCookie(request);
		if(!StringUtil.isEmptyStr(token)){
			return AESCoder.doDecodeStr(ThreeDes.decode(token));
		}		
		return null;
	}	
	
	
	/**
	 * 非过滤器验证是否重复登录
	 * @author: pangshikui
	 * @date: Feb 21, 2019 1:55:12 PM 
	 * @param request
	 * @throws Exception
	 */
	public static void checkSession(HttpServletRequest request) throws Exception {
		String uri = request.getRequestURI();
		TreeMap<String, String> map = getTreeMap(request);
		map.put("uri", uri);
		String userId = getReqUserIdCanNull(request, map.get("suid"));
		if (uri.indexOf(CLIENT) > 0) {
			if (!map.containsKey("sessionid")) {
				throw new BaseException("客户端上传参数不合法！");
			}
			if(!StringUtil.isEmpty(userId) && !"-1".equals(userId)) {
				String sessionId = AESCoder.doDecodeStr(map.get("sessionid"));
				JedisSessionBean jbean =  JedisSessionHandle.getJedisSession(userId);
				if (!jbean.getSession_id().equals(sessionId)) {
					log.info("sessionId:" + sessionId + ",从redis获取的sessionid:"	+ jbean.getSession_id());
					throw new BaseRtException(CodeConstants.SESSION_ERROR_CODE,"您的帐号在别的设备上登录，您被迫下线！");
				}
			}
		}else if (uri.indexOf(HTML) > 0){
			if(!StringUtil.isEmpty(userId) && !"-1".equals(userId)) {
				String sessionid = request.getParameter("sessionid");
				if(!StringUtil.isEmptyStr(sessionid)){
					String sessionId = AESCoder.doDecodeStr(sessionid);
					JedisSessionBean jbean =  JedisSessionHandle.getJedisSession(userId);
					if (!jbean.getSession_id().equals(sessionId)) {
						log.info("sessionId:" + sessionId + ",从redis获取的sessionid:"	+ jbean.getSession_id());
						throw new BaseRtException(CodeConstants.SESSION_ERROR_CODE,"您的帐号在别的设备上登录，您被迫下线！");
					}
				}	
			}
		}else{
			throw new BaseException("非法的请求!");
		}
	}
	
	/**
	 * 获取所有签名参数和参数值并放到treeMap中按key的首字母升序
	 * 
	 * @param request
	 * @return
	 */
	private static TreeMap<String, String> getTreeMap(HttpServletRequest request) {
		log.info("客户端ip:" + request.getRemoteHost() + ",发送请求:");
		TreeMap<String, String> map = new TreeMap<String, String>();
		Enumeration<?> enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			String value = request.getParameter(paraName);
			// 将d_version的值设置成全部小写
			if ("d_version".equals(paraName)) {
				value = value.toLowerCase();
			}
			map.put(paraName, value);
			log.debug("paraName:" + paraName + "|value:" + value);
		}
		return map;
	}
	/**
	 * 获取用户信息
	 * @author zhufeng
	 * @date 2019年2月26日
	 * @param userId
	 * @param mobileNo
	 * @return
	 * @throws BaseException
	 */
	public static UserInfoBean getUserInfoBean(String userId, String mobileNo) throws BaseRtException {
		if ((StringUtil.isEmpty(userId) || "-1".equals(userId)) && StringUtil.isEmpty(mobileNo)) {
			return null;
		}
		JSONObject userInfo;
		userInfo = getUserInfo(userId, mobileNo);
		UserInfoBean userInfoBean = null;
		if (userInfo != null && !StringUtil.isEmpty(JsonUtil.getJsonFieldValueCanNull(userInfo, "driver_id"))) {
			userInfoBean = new UserInfoBean();
			userInfoBean.setDriverId(JsonUtil.getJsonFieldValueCanNull(userInfo, "driver_id"));
			userInfoBean.setDcauserid(JsonUtil.getJsonFieldValueCanNull(userInfo, "dca_user_id"));
			userInfoBean.setHeadImgUrl(JsonUtil.getJsonFieldValueCanNull(userInfo, "head_img_url"));
			userInfoBean.setIdentityLevel(JsonUtil.getJsonFieldValueCanNull(userInfo, "identity_level"));
			userInfoBean.setMobileNo(JsonUtil.getJsonFieldValueCanNull(userInfo, "mobile_no"));
			userInfoBean.setNickName(JsonUtil.getJsonFieldValueCanNull(userInfo, "nick_name"));
			userInfoBean.setRealName(JsonUtil.getJsonFieldValueCanNull(userInfo, "real_name"));
			userInfoBean.setShqLevel(JsonUtil.getJsonFieldValueCanNull(userInfo, "shq_level"));
			userInfoBean.setUserId(JsonUtil.getJsonFieldValueCanNull(userInfo, "user_id"));
			userInfoBean.setOwnerUniqueCode(JsonUtil.getJsonFieldValueCanNull(userInfo, "owner_unique_code"));
			userInfoBean.setRealApproveState(JsonUtil.getJsonFieldValueCanNull(userInfo, "real_approve_state"));
			userInfoBean.setIdentity(JsonUtil.getJsonFieldValueCanNull(userInfo, "identity"));
			userInfoBean.setStatus(JsonUtil.getJsonFieldValueCanNull(userInfo, "status"));
			userInfoBean.setType(JsonUtil.getJsonFieldValueCanNull(userInfo, "type"));
		}
		return userInfoBean;
	}
	
	public static JSONObject getUserInfo(String userId,String mobileNo) throws BaseRtException {
		JSONObject data = new JSONObject();
		data.put("user_id", userId);
		data.put("mobile_no", mobileNo);
		log.info("传入微服务参数："+data.toString());
		String receive = LgmsUtil.postLgms(LgmsConstants.LGMS_1061, data);
		checkLgmsReceive(receive);
		JSONObject dataLgms = new JSONObject(receive).getJSONObject("data");
		String userStr = JsonUtil.getJsonFieldValueCanNull(dataLgms, "user_Desc_Info");
		if(StringUtil.isEmpty(userStr)) {
			return null;
		} else {
			return dataLgms.getJSONObject("user_Desc_Info");
		}
	}
	
	public static void checkLgmsReceive(String receive) throws BaseRtException {
		log.debug("微服务返回"+receive);
		if(!receive.contains("result")) {
			log.info(receive);
			throw new BaseRtException("服务器繁忙，请稍后重试");
		}
		JSONObject jsonObj = new JSONObject(receive).getJSONObject("result");
		String reqCode = jsonObj.getString("req_code");
		if(!"200".equals(reqCode)) {
			log.info(receive);
			throw new BaseRtException("服务器繁忙，请稍后重试");
		}
	}
}
