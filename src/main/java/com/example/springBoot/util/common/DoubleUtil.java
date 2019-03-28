package com.example.springBoot.util.common;

import java.math.BigDecimal;

/**
 * double类型的操作
 * @author yutaotie
 *
 */
public class DoubleUtil {
	
	 /**
     * 修改精度
     * 
     * @param value
     * @param num
     * @return
     */
    public static double changeDecimal(double value, int num) {
        BigDecimal b = new BigDecimal(value);
        double v = b.setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
        return v;
    }

    /**
     * 两个double相加方法
     * 
     * @param a
     * @param b
     * @return
     */
    public static Double doubleAdd(Double a, Double b) {
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return b1.add(b2).doubleValue();
    }

    /**
     * 两个double相加方法,并保留指定精度
     * 
     * @param a
     * @param b
     * @param num
     * @return
     */
    public static Double doubleAdd(Double a, Double b, int num) {
        return changeDecimal(doubleAdd(a, b), num);
    }

    /**
     * 两个double相减方法
     * 
     * @param a
     * @param b
     * @return
     */
    public static Double doubleSub(Double a, Double b) {
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 两个double相减方法,并保留指定精度
     * 
     * @param a
     * @param b
     * @param num
     * @return
     */
    public static Double doubleSub(Double a, Double b, int num) {
        return changeDecimal(doubleSub(a, b), num);
    }

    /**
     * 两个double相乘方法
     * 
     * @param a
     * @param b
     * @return
     */
    public static Double doubleMul(Double a, Double b) {
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 两个double相乘方法,并保留指定精度
     * 
     * @param a
     * @param b
     * @param num
     * @return
     */
    public static Double doubleMul(Double a, Double b, int num) {
        return changeDecimal(doubleMul(a, b), num);
    }

    /**
     * 两个double相除方法,并保留指定精度
     * 
     * @param a
     * @param b
     * @param scale
     * @return
     */
    public static Double doubleDiv(Double a, Double b, int scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return Double.valueOf(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue());
    }
    
    /**
     * 两个double相除方法,并保留指定精度
     * 
     * @param a
     * @param b
     * @param scale
     * @return
     */
    public static Double doubleDiv(Double a, Double b,int scaleType,int scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return Double.valueOf(b1.divide(b2, scale, scaleType).doubleValue());
    }
    
    /**
     * 两个double相除方法,并保留指定精度,并向下取值
     * @param a
     * @param b
     * @param scale
     * @return
     */
    public static String doubleDivStr(Double a, Double b,int scaleType,int scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return b1.divide(b2, scale, scaleType).toString();
    }
    
    
    /**
     * 两个double相除方法,并保留指定精度,并向下取值
     * @param a
     * @param b
     * @param scale
     * @return
     */
    public static String doubleDivStr(Double a, Double b, int scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return b1.divide(b2, scale, BigDecimal.ROUND_DOWN).toString();
    }
    
    /**
     * 保留double的精度值
     * @param doubleStr
     * @param scale
     * @return
     */
    public static String  getDoubleScaleStr(String doubleStr,int scale){
    	Double dou = StringUtil.getEmptyNum(doubleStr).doubleValue();
    	return  doubleDivStr(dou,1d,scale);   	
    }
    
    /**
     * 保留double的精度值
     * @param doubleStr
     * @param scale
     * @return
     */
    public static String  getDoubleStr(double dou,int scale){
    	return  doubleDivStr(dou,1d,scale);   	
    }
    
    /**
     * 保留double的精度值
     * @param doubleStr
     * @param scale
     * @return
     */
    public static String  getDoubleStr(String  doubleStr,int scaleType,int scale){
    	Double dou = StringUtil.getEmptyNum(doubleStr).doubleValue();
    	return  doubleDivStr(dou,1d,scaleType,scale);   	
    }
    
    
    /**
     * 保留double的精度值
     * @param doubleStr
     * @param scale
     * @return
     */
    public static String  getDoubleStr(double dou,int scaleType,int scale){
    	return  doubleDivStr(dou,1d,scaleType,scale);   	
    }
 
    /**
     * 去除小数点后无用的0
     * @author zhufeng
     * @date 2018年8月5日
     * @param s
     * @return
     */
    public static String  getDoubleStr(String s){
    	if(s.indexOf(".") > 0){
    		//正则表达
    		s = s.replaceAll("0+?$", "");//去掉后面无用的零
			s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
		}
    	return  s;   	
    }
}
