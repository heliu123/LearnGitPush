package com.example.springBoot.util.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


public class MedalUtil {
	
	private static final Logger log =  LogManager.getLogger(MedalUtil.class);

	/**
	 * 任命用户勋章
	 * @author zhufeng
	 * @date 2018年9月21日
	 * @param opt_user_id 操作人ID
	 * @param user_id 被操作人ID
	 * @param medal_base_id 勋章ID
	 * @param is_receive 是否立刻佩戴  0是 1否
	 */
	public static void appointMedal(String opt_user_id, String user_id, String medal_base_id, String is_receive) {
		try {
			Map<String, String> map = new HashMap<String,String>();
		//	map.put("opt_user_id", AESCoder.doEncodeStr(opt_user_id));
			map.put("user_id", user_id);
			map.put("medal_base_id", medal_base_id);
			map.put("is_receive", is_receive);
			//String result = LgmsUtil.postLgms(LgmsConstants.LGMS_1016, map);
		//	log.info("用户"+user_id+",勋章任命结果："+result);
		} catch (Exception e) {
			log.error("用户"+user_id+",勋章任命失败！！！！",e);
		}
	}
	
}
