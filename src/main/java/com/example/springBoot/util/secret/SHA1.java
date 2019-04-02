package com.example.springBoot.util.secret;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 服务器签名类
 * **/
public class SHA1 {
	private static Logger LOG = LogManager.getLogger(SHA1.class);
	
	/**
	 * 生成签名方法
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static String getSign(String code) throws Exception {
		LOG.info("生成签名前的字符串为:" + code);
		String eCode = getHmacSHA1(code);
		LOG.info("生成签名后的字符串为:" + eCode);
		return eCode;
	}
	
	private static String getHmacSHA1(String value) {
		try {
			// Get an hmac_sha1 key from the raw key bytes
			byte[] keyBytes = System.getProperty("APP_SN_SECRET_KEY").getBytes();
			//byte[] keyBytes = "aa".getBytes();
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

			// Get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);

			// Compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(value.getBytes());

			// Convert raw bytes to Hex
			String hexBytes = byte2hex(rawHmac);
			// hexBytes = appendEqualSign(hexBytes);
			return hexBytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String byte2hex(final byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0xFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}


	public static String sha1Code(final String decript) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
	
	

}
