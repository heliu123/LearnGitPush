package com.wtyt.tsr.util.common;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.swetake.util.Qrcode;
import com.wtyt.tsr.util.bean.ConvertBean;
import com.wtyt.tsr.util.bean.ImageServerBean;
import com.wtyt.tsr.util.conn.URLConnectionUtil;

import sun.misc.BASE64Encoder;

public class QRCodeUtil {
	
	private static Logger log = Logger.getLogger(QRCodeUtil.class);
	
	public static int BASE_HEIGHT = 500;
	public static int BASE_WIDTH = 500;

	/**
	 * 生成二维码
	 * @throws Exception
	 */
	public static String QRCodeEncoder(String content,String head_img_url) throws Exception {
		Qrcode qrcode = new Qrcode();
		qrcode.setQrcodeErrorCorrect('M');
		qrcode.setQrcodeEncodeMode('B');
		int qrsize=8;
		qrcode.setQrcodeVersion(qrsize);
		
		String testString = content;

		byte[] d = testString.getBytes("UTF-8");

		int imgSize = 67 + 12 * (qrsize-1);
		BufferedImage bi = new BufferedImage(imgSize, imgSize,
				BufferedImage.TYPE_INT_RGB);

		// createGraphics
		Graphics2D g = bi.createGraphics();
		// set background
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, imgSize, imgSize);

		g.setColor(Color.BLACK);

		if (d.length > 0 && d.length <123) {
			boolean[][] b = qrcode.calQrcode(d);

			for (int i = 0; i < b.length; i++) {

				for (int j = 0; j < b.length; j++) {
					if (b[j][i]) {
						g.fillRect(j * 3 + 2, i * 3 + 2, 3, 3);
					}
				}

			}
		}
		if(StringUtils.isEmpty(head_img_url)){
			head_img_url=System.getProperty("HEAD_IMG_URL");
		}
		URI uri=new URI(head_img_url);
		URL url=uri.toURL();
		BufferedImage  img=ImageIO.read(url);
		g.drawImage(img, 55, 55,40,40,null);
		g.dispose();
		bi.flush();
		
        BASE64Encoder encoder = new sun.misc.BASE64Encoder();     
        ByteArrayOutputStream baos = new ByteArrayOutputStream();    
        ImageIO.write(bi, "jpg", baos);    
        byte[] bytes = baos.toByteArray();    
        //System.out.println("生成二维码啦~"); 
        return encoder.encodeBuffer(bytes).trim(); 
		
	}
	
	public static String getQrCodeBase64(String text,String tempFilePath,String fileName) throws IOException{ 
        String format = "png";  
        Hashtable hints = new Hashtable();  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
        BitMatrix bitMatrix = null;  
        try {  
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, BASE_WIDTH, BASE_HEIGHT, hints);  
            bitMatrix = deleteWhite(bitMatrix);//删除白边
        } catch (WriterException e) {  
            e.printStackTrace();  
        }    
        //直接写入文件  
        File outputFile = new File(tempFilePath); 
        if(null==outputFile||!outputFile.exists()){
        	outputFile.mkdirs();
        }
        outputFile = new File(tempFilePath+fileName);  
        MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);    
        //通过流写入文件，不需要flush()  
        OutputStream os1 = new FileOutputStream(tempFilePath+fileName);  
        MatrixToImageWriter.writeToStream(bitMatrix, format, os1);    
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);  
        ByteArrayOutputStream os = new ByteArrayOutputStream();//新建流。  
        ImageIO.write(image, format, os);//利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。  
        byte b[] = os.toByteArray();//从流中获取数据数组。  
        String str = new BASE64Encoder().encode(b); 
        return str;
	}
	
	/**
	 * 执行上传图片
	 * @param img_base64_url
	 * @param fileName
	 * @return
	 */
	public static String executeUploadeImage(String img_base64_url,String fileName){
		ImageServerBean imageServerBean = new ImageServerBean();
		imageServerBean.setFile_name(fileName);
		imageServerBean.setFile_type(".jpg");
		imageServerBean.setPhoto_stream(StreamUtil.gZip(StreamUtil.getByteFromBase64(img_base64_url)));
        imageServerBean.setHeight("300");
		imageServerBean.setWidth("300");
		imageServerBean.setFormat("jpg");
		imageServerBean.setIsUseCdn("1");
		imageServerBean.setIsAddSy("0");
		return getImageServerInfo(imageServerBean);
	}
	
	
	/**
	 * 上传图片服务器并获取相关的信息
	 * @param imageUrl
	 * @return 用户选择的图片ID
	 */
	private static String getImageServerInfo(ImageServerBean imageServerBean) {
		log.info("进入getQrCodeUrl");
		try {
			JSONStringer json = new JSONStringer();
			json.object();
			String file_name = imageServerBean.getFile_name();
			if(StringUtil.isEmpty(imageServerBean.getFile_type())){
				file_name+=".jpg";
			}else{
				file_name+=imageServerBean.getFile_type();
			}
			json.key("checkValue").value(ConvertBean.md5(file_name+"uhF64gfg31H"));
			json.key("fileName").value(file_name);
			json.key("typeCode").value("shq");
			json.key("zipSave").value("0");
			json.key("isAddSy").value(imageServerBean.getIsAddSy());
			json.key("isUseCdn").value(imageServerBean.getIsUseCdn());
			json.key("dataField").value(imageServerBean.getPhoto_stream());
			JSONArray array = new JSONArray();
			JSONObject js = new JSONObject();
			js.put("width",imageServerBean.getWidth());
			js.put("height", imageServerBean.getHeight());
			js.put("imgType", imageServerBean.getFormat());
			array.put(js);
			json.key("typeField").value(array);					
			json.endObject();
			//log.info("传递参数:"+json.toString());
			/* 2017-05-11 修改了图片上传url 去除两次encode */
			log.info(">>>>>>>>>看看访问的传参"+json.toString());
			log.info(">>>>>>>>>看看访问的url"+System.getProperty("PHOTO_SERVER_URL_NEW"));
			String postStr = ConvertBean.twoTimesDecode(URLConnectionUtil.doPost(System.getProperty("PHOTO_SERVER_URL_NEW"), json.toString()));
			System.out.println("返回结果="+postStr);
			JSONObject resultJson = new JSONObject(postStr);
			if("0000".equals(resultJson.get("reCode"))){
				log.info("离开getQrCodeUrl");
				//2015-10-22 上传图片重新处理
				JSONObject imgUrlObj = resultJson.getJSONObject("reurls");
				String img_url = imgUrlObj.getString(imageServerBean.getFile_name());//原图地址
				return img_url;
			}else{
				return "";
			}
		} catch (Exception e) {	
			log.error("执行getQrCodeUrl出错了，原因=="+e.getMessage());
			return "";
		}
	}
	
	
	private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;
 
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

}
