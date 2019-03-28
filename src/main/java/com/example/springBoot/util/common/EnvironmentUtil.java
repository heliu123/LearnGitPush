package com.example.springBoot.util.common;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 环境变量相关的类
 * @author 御饕餮
 * @date 2018年7月26日 上午8:40:38
 */
@Component
public class EnvironmentUtil implements EnvironmentAware{
	
	private static Environment env;

	@Override
	public void setEnvironment(Environment environment) {
		env = environment;
	}
	
	
	/**
	 * 获取环境变量内容
	 * @param envName
	 * @param targetType
	 * @param defaultValue
	 * @return
	 */
	public static <T> T getEnvValue(String envName,Class<T> targetType,T defaultValue){
		if(null==env){
		//	throw new BaseRtException("当前环境变量未注入到beanFactory！！！！");
		}	
		return env.getProperty(envName, targetType, defaultValue);
	}

}
