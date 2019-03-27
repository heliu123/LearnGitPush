package com.wtyt.tsr.util.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.wtyt.tsr.util.bean.ConvertBean;
import com.wtyt.tsr.util.bean.ImageServerBean;
import com.wtyt.tsr.util.conn.URLConnectionUtil;
import com.wtyt.tsr.util.constants.ImgConstants;

/**
 * 此类用于与图片服务器进行交互 ImageServerBean中设置的高度/宽度以按比例缩小后 最后达到设置值为准。不进行图片变形。 例
 * 400*200图标按200*200压缩后 得出的图片是200*100
 * 
 * @author zhufeng
 *
 */
public class ImageServerUtil {

	private static Logger log = LogManager.getFormatterLogger(ImageServerUtil.class);

	public static void main(String[] args) {
		/** main方法调试使用System.setProperty设置系统参数 程序启动后会自动从apollo获取,不用单独赋值 */
		System.setProperty("PHOTO_SERVER_URL_NEW",
				"http://220.248.226.76:20208/image_server/imageUploadHandleV3.action?");
		System.setProperty("IS_SHQ_TEST_ENVIRONMENT", "0");
		// 开始初始化图片bean
		ImageServerBean imageServerBean = new ImageServerBean();
		// 设置图片名，不能使用中文
		imageServerBean.setFile_name("myfile");
		// 设置图片文件名后缀
		imageServerBean.setFile_type("."+ImgConstants.IMG_TYPE_JPG);
		// 获取图片base64字符串
		String img_url = "http://pic1.nipic.com/2008-12-30/200812308231244_2.jpg";
		String img_base64_url = ImageServerUtil.Image2Base64(img_url);
		// 压缩图片base64字符串
		imageServerBean.setPhoto_stream(StreamUtil.gZip(StreamUtil.getByteFromBase64(img_base64_url)));
		// 设置图片高度
		imageServerBean.setHeight("200");
		// 设置图片宽度
		imageServerBean.setWidth("300");
		// 设置文件格式
		imageServerBean.setFormat(ImgConstants.IMG_TYPE_JPG);
		// 设置图片存储位置
		imageServerBean.setPath(ImgConstants.TSR_UPLOAD_IMG_PATH);
		// 设置是否使用cdn 资源图片统一使用CDN
		imageServerBean.setIsUseCdn("1");
		// 设置是否添加水印
		imageServerBean.setIsAddSy("0");
		/** 上传图片 */
		imageServerBean = ImageServerUtil.getImageServerInfo(imageServerBean);
		System.out.println(imageServerBean.getImg_url());
		System.out.println(imageServerBean.getSmall_url());
	}

	/**
	 * 远程读取image转换为Base64字符串
	 * 
	 * @param imgUrl
	 * @return
	 */
	public static String Image2Base64(String imgUrl) {
		URL url = null;
		InputStream is = null;
		ByteArrayOutputStream outStream = null;
		HttpURLConnection httpUrl = null;
		try {
			url = new URL(imgUrl);
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.connect();
			httpUrl.getInputStream();
			is = httpUrl.getInputStream();

			outStream = new ByteArrayOutputStream();
			// 创建一个Buffer字符串
			byte[] buffer = new byte[1024];
			// 每次读取的字符串长度，如果为-1，代表全部读取完毕
			int len = 0;
			// 使用一个输入流从buffer里把数据读取出来
			while ((len = is.read(buffer)) != -1) {
				// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
				outStream.write(buffer, 0, len);
			}
			// 对字节数组Base64编码
			return new String(Base64.getEncoder().encode(outStream.toByteArray()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpUrl != null) {
				httpUrl.disconnect();
			}
		}
		return imgUrl;
	}

	/**
	 * 上传图片服务器并获取相关的信息(上传成功后img_url/small_url被替换)
	 * 
	 * @author zhufeng
	 * @date 2019年3月1日
	 * @param imageServerBean
	 * @return ImageServerBean
	 */
	public static ImageServerBean getImageServerInfo(ImageServerBean imageServerBean) {
		log.info("进入getImageServerInfo");
		try {
			JSONStringer json = new JSONStringer();
			json.object();
			String file_name = imageServerBean.getFile_name();
			if (StringUtil.isEmpty(imageServerBean.getFile_type())) {
				file_name += ".jpg";
			} else {
				file_name += imageServerBean.getFile_type();
			}
			json.key("checkValue").value(ConvertBean.md5(file_name + "uhF64gfg31H"));
			json.key("fileName").value(file_name);
			json.key("typeCode").value(imageServerBean.getPath());
			json.key("zipSave").value("1");
			json.key("isAddSy").value(imageServerBean.getIsAddSy());
			json.key("isUseCdn").value(imageServerBean.getIsUseCdn());
			json.key("dataField").value(imageServerBean.getPhoto_stream());
			JSONArray array = new JSONArray();
			JSONObject js = new JSONObject();
			js.put("width", imageServerBean.getWidth());
			js.put("height", imageServerBean.getHeight());
			js.put("imgType", imageServerBean.getFormat());
			array.put(js);
			json.key("typeField").value(array);
			json.endObject();
			// log.info("传递参数:"+json.toString());
			/* 2017-05-11 修改了图片上传url 去除两次encode */
			String postStr = ConvertBean.twoTimesDecode(
					URLConnectionUtil.doPost(System.getProperty("PHOTO_SERVER_URL_NEW"), json.toString()));
			JSONObject resultJson = new JSONObject(postStr);
			if ("0000".equals(resultJson.get("reCode"))) {
				JSONObject imgUrlObj = resultJson.getJSONObject("reurls");
				String imgUrl = imgUrlObj.getString(imageServerBean.getFile_name());
				String smallUrl = imgUrlObj.getString(imageServerBean.getFile_name() + imageServerBean.getWidth() + "x" + imageServerBean.getHeight());
				if ("0".equals(System.getProperty("IS_SHQ_TEST_ENVIRONMENT"))) {
					//测试环境按测试的方式处理图片
					imgUrl = StringUtil.isEmpty(imgUrl)?"":imgUrl.replace("http://shq.img.log56.com:20209/", "https://imgttest.log56.com/");
					smallUrl = StringUtil.isEmpty(smallUrl)?"":smallUrl.replace("http://shq.img.log56.com:20209/", "https://imgttest.log56.com/");
				} else {
					//其他是按正式环境处理
					imgUrl = StringUtil.isEmpty(imgUrl)?"":imgUrl.replace("http://", "https://");
					smallUrl = StringUtil.isEmpty(smallUrl)?"":smallUrl.replace("http://", "https://");
				}
				imageServerBean.setImg_url(imgUrl);
				imageServerBean.setSmall_url(smallUrl);
				log.info("离开getImageServerInfo");
				return imageServerBean;
			} else {
				return imageServerBean;
			}
		} catch (Exception e) {
			log.error("执行getQrCodeUrl出错了，原因==" + e.getMessage());
			return imageServerBean;
		}
	}

}
