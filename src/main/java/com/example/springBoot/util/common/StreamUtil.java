package com.wtyt.tsr.util.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.bzip2.CBZip2OutputStream;

import sun.misc.BASE64Decoder;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZInputStream;
import com.jcraft.jzlib.ZOutputStream;

public class StreamUtil {
	/***
	 * 压缩GZip
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] gZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(bos);
			gzip.write(data);
			gzip.finish();
			gzip.close();
			b = bos.toByteArray();
			bos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	/***
	 * 解压GZip
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] unGZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			GZIPInputStream gzip = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			b = baos.toByteArray();
			baos.flush();
			baos.close();
			gzip.close();
			bis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	/***
	 * 压缩Zip
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] zip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ZipOutputStream zip = new ZipOutputStream(bos);
			ZipEntry entry = new ZipEntry("zip");
			entry.setSize(data.length);
			zip.putNextEntry(entry);
			zip.write(data);
			zip.closeEntry();
			zip.close();
			b = bos.toByteArray();
			bos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	/***
	 * 解压Zip
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] unZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ZipInputStream zip = new ZipInputStream(bis);
			while (zip.getNextEntry() != null) {
				byte[] buf = new byte[1024];
				int num = -1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((num = zip.read(buf, 0, buf.length)) != -1) {
					baos.write(buf, 0, num);
				}
				b = baos.toByteArray();
				baos.flush();
				baos.close();
			}
			zip.close();
			bis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	/***
	 * 压缩BZip2
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] bZip2(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			CBZip2OutputStream bzip2 = new CBZip2OutputStream(bos);
			bzip2.write(data);
			bzip2.flush();
			bzip2.close();
			b = bos.toByteArray();
			bos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	/***
	 * 解压BZip2
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] unBZip2(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			CBZip2InputStream bzip2 = new CBZip2InputStream(bis);
			byte[] buf = new byte[1024];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = bzip2.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			b = baos.toByteArray();
			baos.flush();
			baos.close();
			bzip2.close();
			bis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	/**
	 * 把字节数组转换成16进制字符串
	 * 
	 * @param bArray
	 * @return
	 */
	public static String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 压缩数据
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static byte[] jzlib(byte[] object) {
		byte[] data = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ZOutputStream zOut = new ZOutputStream(out,
					JZlib.Z_DEFAULT_COMPRESSION);
			DataOutputStream objOut = new DataOutputStream(zOut);
			objOut.write(object);
			objOut.flush();
			zOut.close();
			data = out.toByteArray();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 解压被压缩的数据
	 * 
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static byte[] unjzlib(byte[] object) {
		byte[] data = null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(object);
			ZInputStream zIn = new ZInputStream(in);
			byte[] buf = new byte[1024];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = zIn.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			data = baos.toByteArray();
			baos.flush();
			baos.close();
			zIn.close();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	
	public static byte[] getHtmlByteArray(final String url) {
		 URL htmlUrl = null;     
		 InputStream inStream = null;     
		 try {         
			 htmlUrl = new URL(url);         
			 URLConnection connection = htmlUrl.openConnection();         
			 HttpURLConnection httpConnection = (HttpURLConnection)connection;         
			 int responseCode = httpConnection.getResponseCode();         
			 if(responseCode == HttpURLConnection.HTTP_OK){             
				 inStream = httpConnection.getInputStream();         
			  }     
			 } catch (MalformedURLException e) {               
				 e.printStackTrace();     
			 } catch (IOException e) {              
				e.printStackTrace();    
		  } 
		byte[] data = inputStreamToByte(inStream);

		return data;
	}
	
	public static byte[] inputStreamToByte(InputStream is) {
		try{
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * 根据base64的字符串生成byte[]
	 * @param base64Str
	 * @return
	 */
	public static byte[] getByteFromBase64(String base64Str) {
        if (base64Str == null){
        	return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        if(base64Str.indexOf(";base64,") != -1){
        	base64Str = base64Str.split(";base64,")[1];
        }
        try {
            //Base64解码  
            byte[] b = decoder.decodeBuffer(base64Str);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据  
                    b[i] += 256;
                }
            }
            return b;
        } catch (Exception e) {
        	return null;
        }
    }
	
	public static void main(String[] args) {
		String s = "this is a test";
		byte[] b1 = zip(s.getBytes());
		System.out.println("zip:" + bytesToHexString(b1));
		byte[] b2 = unZip(b1);
		System.out.println("unZip:" + new String(b2));
		byte[] b3 = bZip2(s.getBytes());
		System.out.println("bZip2:" + bytesToHexString(b3));
		byte[] b4 = unBZip2(b3);
		System.out.println("unBZip2:" + new String(b4));
		byte[] b5 = gZip(s.getBytes());
		System.out.println("bZip2:" + bytesToHexString(b5));
		byte[] b6 = unGZip(b5);
		System.out.println("unBZip2:" + new String(b6));
		byte[] b7 = jzlib(s.getBytes());
		System.out.println("jzlib:" + bytesToHexString(b7));
		byte[] b8 = unjzlib(b7);
		System.out.println("unjzlib:" + new String(b8));
	}
}