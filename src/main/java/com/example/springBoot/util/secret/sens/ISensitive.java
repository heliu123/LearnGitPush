package com.example.springBoot.util.secret.sens;

/**
 * 敏感词的解密加密方法
 * @author 御饕餮
 * @date 2018年8月2日 上午10:05:47
 */
public interface ISensitive {
	
	/**
	 * 加密的方法
	 * @return
	 */
	public String encode(String value);
	
	/**
	 * 解密的方法
	 * @return
	 */
	public String decode(String value);

}
