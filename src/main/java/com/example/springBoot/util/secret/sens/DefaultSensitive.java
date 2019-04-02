package com.example.springBoot.util.secret.sens;


import com.example.springBoot.util.common.StringUtil;
import com.example.springBoot.util.secret.AESCoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 默认的加密解密方法
 * @author 御饕餮
 * @date 2018年8月2日 上午10:07:53
 */
public class DefaultSensitive implements ISensitive{
	
	private static final Logger log = LogManager.getLogger(DefaultSensitive.class);

	@Override
	public String encode(String value) {
		if(StringUtil.isEmptyStr(value)){
			return value;
		}
		try{
			return AESCoder.doEncodeStr(value);
		}catch(Exception e){
			log.debug("warning!!!!!加密"+value+"失败！！将返回原值！！！");
		}
		return value;
	}

	@Override
	public String decode(String value) {
		if(StringUtil.isEmptyStr(value)){
			return value;
		}
		try{
			return AESCoder.doDecodeStr(value);
		}catch(Exception e){
			log.debug("warning!!!!!解密"+value+"失败！！将返回原值！！！");
		}
		return value;
	}

}
