package com.example.springBoot.util.common;
/*import java.text.ParseException;
import java.text.SimpleDateFormat;*/

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class DateUtil {
	
	public static final String FORMATE_YMD_HMS_MS ="yyyy-MM-dd HH:mm:ss.SSS";
	public static final String FORMATE_YMD_HMS ="yyyy-MM-dd HH:mm:ss";
	public static final String FORMATE_YMD ="yyyy-MM-dd";
	public static final String FORMATE_YMD2 ="yyyy.MM.dd";
	public static final String FORMATE_YMD3 ="yyyy年MM月dd日";
	public static final String FORMATE_YM ="yyyy-MM";
	public static final String FORMATE_Y ="yyyy";
	public static final String FORMATE_MM_DD ="MM-dd";
	public static final String FORMATE_MM_DD2 ="MM月dd日";
	public static final String FORMATE_YR_SF = "MM-dd HH:mm";
	
	private static int weeks = 0;

	public static int getSecondDiffWithNow(Date targetDate) {
		return (int)( targetDate.getTime() - getDate().getTime())/ 1000;
	}
	
	public static Date addDays(int days) {
		Date current = getDate();
		current = DateUtils.addDays(current, days);
		return current;
	}
	
	public static Date addDays(Date date,int days) {
		Date current = date;
		current = DateUtils.addDays(current, days);
		return current;
	}
	
	public static String addDays(String date,int days) throws Exception {
		Date current = parseDate(date,FORMATE_YMD);
		current = DateUtils.addDays(current, days);
		return formatDate(current, FORMATE_YMD);
	}
	
	public static Calendar getCalendar(){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		return calendar;
	}
	
	public static Date getDate(){
		return getCalendar().getTime();
	}
	
	
	/**
	 * 获得指定的月份
	 * @param num
	 * @return
	 */
	public static String getMonth(int num){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -num);
		Date theDate = calendar.getTime();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		return df.format(gcLast.getTime());
	}
	
	public static String getMonth(int num,String format){
		Calendar c = Calendar.getInstance();
    	c.add(Calendar.MONTH, -num);
    	return formatDate(c.getTime(),format);
	}
	
	public static Timestamp getTimestamp(){
		return new Timestamp(getDate().getTime());
	}
	public static Timestamp getTimestamp(long time){
		return new Timestamp(time);
	}
	
	public static String formatDate(Date date,String format){
		if(date == null){return "";}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static Date parseDate(String dateStr,String format) throws  Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateStr);
	}
	
	
	public static String getCurrentDate() {
		return DateUtil.formatDate(DateUtil.getDate(), "yyyy-MM-dd");
	}
	public static String getCurrentDate(String format) {
		return DateUtil.formatDate(DateUtil.getDate(), format);
	}
	
	/**
	 * 获得指定时间段的月份集合
	 * @param beginDateStr
	 * @param endDateStr
	 * @return
	 */
	public static String getMonthList(String beginDateStr, String endDateStr) {
        //指定要解析的时间格式
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        //返回的月份列表
        String sRet = "";
        
        //定义一些变量
        Date beginDate = null;
        Date endDate = null;
        
        GregorianCalendar beginGC = null;
        GregorianCalendar endGC = null;
        
        try {
            //将字符串parse成日期
            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);
            
            //设置日历
            beginGC = new GregorianCalendar(); 
            beginGC.setTime(beginDate); 
            
            endGC = new GregorianCalendar(); 
            endGC.setTime(endDate);
            
            //直到两个时间相同
            while(beginGC.getTime().compareTo(endGC.getTime())<=0){
                //累加字符串,用单引号分隔
                if (sRet.equals("")) {
                    sRet += beginGC.get(Calendar.YEAR) + "-" + (beginGC.get(Calendar.MONTH)+1);
                }
                else {
                    sRet += "," + beginGC.get(Calendar.YEAR) + "-" + (beginGC.get(Calendar.MONTH)+1);    
                }
                
                //以月为单位，增加时间
                beginGC.add(Calendar.MONTH,1);
            }
            return sRet;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	/**
	 * 返回指定两个时间点的月份集合
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static String getBetwwenMonthList(String beginTime,String endTime){
		StringBuilder sb = new StringBuilder();
		String months = getMonthList(beginTime,endTime);
		String[] mh = months.split(",");
		for(String m : mh){
			String year = m.substring(0,m.indexOf("-"));
			String month = m.substring(m.indexOf("-")+1);
			if(Integer.parseInt(month)<10){
				month ="0"+month;
			}
			String ms = year+"-"+month;
			
			sb.append(ms+",");
		}
		return sb.substring(0, sb.lastIndexOf(","));
	}
	
	/**
	 * 获得投诉问题统计月份列表,形如:'2012-02','2013-03'
	 * @return
	 */
	public static String getQuestionCptMonthList(String beginTime,String endTime){
		String monthList  = getBetwwenMonthList(beginTime, endTime);
		String[] monthArray = monthList.split(","); 
		StringBuilder sb  = new StringBuilder();
		for(String s : monthArray){
			String str ="";
			str ="'"+s+"'";
			sb.append(str);
			sb.append(",");
		}
		return sb.substring(0, sb.lastIndexOf(","));
	}
	
	/**
	 * 当前时间和传入时间比对
	 * @param date
	 * @return
	 */
	public static  boolean compareDate(String date){
		try {
			Date cDate = parseDate(date,FORMATE_YMD);
			if(getDate().before(cDate)){
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 两个时间进行比对
	 * @param date
	 * @return
	 */
	public static  boolean compareDate(String beginTime,String endTime){
		try {
			Date sDate = parseDate(beginTime,FORMATE_YMD);
			Date eDate = parseDate(endTime,FORMATE_YMD);
			if(sDate.before(eDate)){
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	 /**
     * 取得指定月份的第一天
     * @param strdate String
     * @return String
	 * @throws Exception 
     */
    public static String getMonthBegin(String strdate) throws Exception
    {
        Date date = parseDate(strdate,FORMATE_YM);
        return formatDate(date,FORMATE_YM) + "-01";
    }

    /**
     * 取得指定月份的最后一天
     *
     * @param strdate String
     * @return String
     * @throws Exception 
     */
    public static String getMonthEnd(String strdate) throws Exception
    {
        Date date = parseDate(getMonthBegin(strdate),FORMATE_YM);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return formatDate(calendar.getTime(),FORMATE_YMD);
    }
    
    /**
     * 输出指定格式的时间字符串
     * @param date
     * @param format
     * @throws Exception 
     */
    public static String getFormatDate(String date,String format) throws Exception{
    	return formatDate(parseDate(date,format),format);
    }
    
    public static String getFormatDate(String date,String format,String format2) throws Exception{
    	return formatDate(parseDate(date,format),format2);
    }
    
    public static String getTime(){
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.MONTH, -1);
    	return formatDate(c.getTime(),FORMATE_YMD);
    }
    
    

    /**
     * 获得指定日期相差的天数
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays(String date1, String date2)
    {
      if ((date1 == null) || (date1.equals("")))
      {
        return 0L;
      }
      if ((date2 == null) || (date2.equals("")))
      {
        return 0L;
      }

      SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");

      Date date = null;

      Date mydate = null;
      try
      {
        date = myFormatter.parse(date1);


        mydate = myFormatter.parse(date2);
      }
      catch (Exception localException)
      {
      }

      long day = (date.getTime() - mydate.getTime()) / 86400000L;

      return day;
    }


     
    /**
     * 获取本月第一天日期
     * @return
     */
    public static String getFirstDayOfMonth()
    {
      String str = "";

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

      Calendar lastDate = Calendar.getInstance();

      lastDate.set(5, 1);

      str = sdf.format(lastDate.getTime());

      return str;
    }
    
    /**
     * 获取本月最后一天日期
     * @return
     */
    public static String getDefaultDay()
    {
      String str = "";

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

      Calendar lastDate = Calendar.getInstance();

      lastDate.set(5, 1);

      lastDate.add(2, 1);

      lastDate.add(5, -1);

      str = sdf.format(lastDate.getTime());

      return str;
    }


    /**
     * 获取本周日的日期
     * @return
     */
    public static String getCurrentWeekday()
    {
      weeks = 0;
      int mondayPlus = getMondayPlus();

      GregorianCalendar currentDate = new GregorianCalendar();

      currentDate.add(5, mondayPlus + 6);

      Date monday = currentDate.getTime();

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

      String preMonday = sdf.format(monday);

      return preMonday;
    }



    private static int getMondayPlus()
    {
      Calendar cd = Calendar.getInstance();


      int dayOfWeek = cd.get(7) - 1;


      if (dayOfWeek == 1)
      {
        return 0;
      }

      return (1 - dayOfWeek);
    }


    /**
     * 获取本周一日期
     * @return
     */
    public static String getMondayOFWeek()
    {
      weeks = 0;
      int mondayPlus = getMondayPlus();
      GregorianCalendar currentDate = new GregorianCalendar();
      currentDate.add(5, mondayPlus);

      Date monday = currentDate.getTime();

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

      String preMonday = sdf.format(monday);

      return preMonday;
    }


    /**
     * 获取上周日日期
     * @return
     */
    public static String getPreviousWeekSunday()
    {
      weeks = 0;
      weeks -= 1;
      int mondayPlus = getMondayPlus();
      GregorianCalendar currentDate = new GregorianCalendar();

      currentDate.add(5, mondayPlus + weeks);

      Date monday = currentDate.getTime();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String preMonday = sdf.format(monday);
      return preMonday;
    }

    
    /**
     * 获取上周一日期
     * @return
     */
    public static String getPreviousWeekday()
    {
      weeks = 0;
      weeks -= 1;
      int mondayPlus = getMondayPlus();

      GregorianCalendar currentDate = new GregorianCalendar();
      currentDate.add(5, mondayPlus + 7 * weeks);
      Date monday = currentDate.getTime();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String preMonday = sdf.format(monday);
      return preMonday;
    }

    
 

    
    /**
     * 获取上月最后一天的日期
     * @return
     */
    public static String getPreviousMonthEnd()
    {
      String str = "";
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Calendar lastDate = Calendar.getInstance();
      lastDate.add(2, -1);
      lastDate.set(5, 1);
      lastDate.roll(5, -1);
      str = sdf.format(lastDate.getTime());
      return str;
    }
    
    /**
     * 下一年的当前时间
     * */
    public static String nextYearDae(){
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.YEAR,1);
    	SimpleDateFormat df = new SimpleDateFormat(FORMATE_YMD);
    	//System.out.println(df.format(c.getTime()));
    	return df.format(c.getTime());
    	
    }
    
    /**
     * 获得指定的年份
     * */
    public static String getYearDae(int year){
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.YEAR,year);
    	SimpleDateFormat df = new SimpleDateFormat(FORMATE_YMD);
    	//System.out.println(df.format(c.getTime()));
    	return df.format(c.getTime());
    	
    }
    /**
     * 下一年的当前时间
     * */
    public static String nextYearDay(){
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.YEAR,1);
    	c.add(Calendar.DATE, 1);
    	SimpleDateFormat df = new SimpleDateFormat(FORMATE_YMD);
    	//System.out.println(df.format(c.getTime()));
    	return df.format(c.getTime());
    	
    }
    
 
    
    /**
     * 获得指定日期年月日的输出格式
     * @param time
     * @param format
     * @return
     * @throws Exception
     */
    public static String getDateForFormat(String time,String format){
    	StringBuilder dateSb;
		try {
			String date = getFormatDate(time,format);
			dateSb = new StringBuilder();
			dateSb.append(date.substring(0, 4));
			dateSb.append("年");
			String dt= date.substring(5,6);
			if("0".equals(dt)){
				dateSb.append(date.substring(6, 7));
			}else{
				dateSb.append(date.substring(5, 7));
			}
			dateSb.append("月");
			dateSb.append(date.substring(8,date.length()));
			dateSb.append("日");
		} catch (Exception e) {
			return null;
		}
    	
    	return dateSb.toString();
    }
    
	// 前一个月的最后一天
	public static String getPreviousMonthLast() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 0);// 设为当前月的1号
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 前一个月的第一天
	public static String getPreviousMonthFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);
		lastDate.add(Calendar.MONTH, -1);
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 当月的第一天
	public static String getCurrentMonthFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获取今天日期
	public static String getTodayTime() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		str = sdf.format(lastDate.getTime());
		return str;
	}

	public static String getYesterdayTime() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.DATE, -1);
		str = sdf.format(lastDate.getTime());
		return str;
	}
    
	  /** 
     * 获取当前时间的的前几天 或者后几天 
     * @param cl 
     * @return 
     */  
	public static String getDay(int number){  
    	String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.DATE, number);
		str = sdf.format(lastDate.getTime());
		return str;
    }  
	/**
	 * 判断当前时间是否在两个时间之间
	 * @param startTime
	 * @param endTime
	 * @param currentTime
	 * @return
	 */
	public static boolean isBetween(Date startTime,Date endTime,Date currentTime){
		if(startTime == null || endTime == null || currentTime== null){
			return false;
		}
		if(currentTime.getTime() > startTime.getTime() && currentTime.getTime() <= endTime.getTime()){
			return true;
		}
		return false;
		
	}
	public static Date getDate(String dateTime,String format){
		if(!StringUtils.isEmpty(dateTime)){
			try {
				SimpleDateFormat sdf =   new SimpleDateFormat(format);
				return sdf.parse(dateTime);
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}
	
	
	  /**
     * 获得指定日期相差的毫秒
     * @param date1
     * @param date2
     * @return
     */
    public static long getMillisecond(String date1, String date2)
    {
      if ((date1 == null) || (date1.equals("")))
      {
        return 0L;
      }
      if ((date2 == null) || (date2.equals("")))
      {
        return 0L;
      }

      SimpleDateFormat myFormatter = new SimpleDateFormat(FORMATE_YMD_HMS_MS);

      Date date = null;

      Date mydate = null;
      try
      {
        date = myFormatter.parse(date1);


        mydate = myFormatter.parse(date2);
      }
      catch (Exception localException)
      {
      }

      long millisecond = date.getTime() - mydate.getTime();

      return millisecond;
    }
    
    
    /**
     * 获取几小时前的时间
     * @return
     */
    public static String beforeOneHourToNowDate(int hours,String format) {
    	Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hours);
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(calendar.getTime());
    } 
    
    public static Map<Object,Object> dateDiff(String startTime, String endTime, String format) throws ParseException {
    	//按照传入的格式生成一个simpledateformate对象
    	SimpleDateFormat sd = new SimpleDateFormat(format);
    	long nd = 1000*24*60*60;//一天的毫秒数
    	long nh = 1000*60*60;//一小时的毫秒数
    	long nm = 1000*60;//一分钟的毫秒数
    	long ns = 1000;//一秒钟的毫秒数long diff;try {
    	//获得两个时间的毫秒时间差异
    	long diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
    	long day = diff/nd;//计算差多少天
    	long hour = diff%nd/nh;//计算差多少小时
    	long min = diff%nd%nh/nm;//计算差多少分钟
    	long sec = diff%nd%nh%nm/ns;//计算差多少秒//输出结果
    	Map<Object,Object> map = new HashMap<Object,Object>();
    	map.put("day",Integer.parseInt(String.valueOf(day)));
    	map.put("hour", Integer.parseInt(String.valueOf(hour)));
    	map.put("min", Integer.parseInt(String.valueOf(min)));
    	map.put("sec", Integer.parseInt(String.valueOf(sec)));
//    	System.out.println("时间相差："+day+"天"+hour+"小时"+min+"分钟"+sec+"秒。");
    	return map;
    }
    
    
    
    /**
     * 获取指定的时间
     * mm-dd
     * 昨天
     * x小时前
     * x分钟前
     * 刚刚
     * 规则1:时间在昨天0点之前的，返回mm-dd
     * 规则2:时间在昨天0点之后今天0点之前的，返回昨天
     * 规则3:时间在今天凌晨之后并且时间差>=1时间，返回x小时前
     * 规则4:5分钟<时间差<1小时，返回x分钟前
     * 规则5:时间差<=5分钟，返回刚刚
     * @param time年月日时分秒
     * @return
     */
    public static String getSqTime(String time){
    	if(StringUtil.isEmpty(time)){
    		return "";
    	}
    	String resultTime = time;
    	try {
			Date paramTime = parseDate(time, FORMATE_YMD_HMS);//传入的时间转换为日志格式的时间
			Date lastDayZeroTime = parseDate(getDay(-1)+" 00:00:00", FORMATE_YMD_HMS);//昨天0点
			Date toDayZeroTime = parseDate(getCurrentDate()+" 00:00:00", FORMATE_YMD_HMS);//今天0点
			Date oneHourTime = parseDate(beforeOneHourToNowDate(1, FORMATE_YMD_HMS),FORMATE_YMD_HMS);//一小时前的时间
			Map<Object,Object> map = dateDiff(time, getCurrentDate(FORMATE_YMD_HMS), FORMATE_YMD_HMS);//时间差	
			int hour = (Integer) map.get("hour");//差几小时
			int min = (Integer) map.get("min");//差几分钟
			
			if(lastDayZeroTime.after(paramTime)){//返回mm-dd
				String t1 = getFormatDate(time, FORMATE_YMD);
//				t1 = t1.substring(t1.length()-5,t1.length());
//				resultTime =t1.replaceAll("0", "");
				/**阿金修改，不会写正则啊，只能用土方法了，郁闷**/
				String[] tt1 = t1.split("-");
				resultTime = Integer.parseInt(tt1[1])+"-"+Integer.parseInt(tt1[2]);
			}else if(lastDayZeroTime.before(paramTime) && toDayZeroTime.after(paramTime)){//返回昨天
				resultTime ="昨天";
			}else if(toDayZeroTime.before(paramTime) && oneHourTime.after(paramTime)){//返回几小时前
		    	resultTime =hour+"小时前";
			}else if(min>5 && min<60){
				resultTime =min+"分钟前";
			}else if(min<=5){
				resultTime ="刚刚";
			}
		} catch (Exception e) {
			//Util.printExceptionInfo(e);
		}
    	return resultTime;
    }
    
    /**
     * 自定义的日期转换格式,还没写完
     * @param time
     * @param format
     * @return
     * @throws Exception
     */
    public static String getDateStrForFormat(String time,String format) throws Exception{
    	if(StringUtil.isEmpty(time)){
    		return "";
    	}
    	String[] mulTime = time.split("-");
    	if(FORMATE_MM_DD2.equals(format)){
    		String monthStr = mulTime[2];
    		if(monthStr.indexOf(" ")!= -1){
    			monthStr = monthStr.split(" ")[0];
    		}
    		return mulTime[1]+"月"+monthStr+"日";
    	}
    	return time;
    }
    

}
