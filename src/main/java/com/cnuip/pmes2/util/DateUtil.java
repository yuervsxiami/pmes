package com.cnuip.pmes2.util;
/**
* Create By Crixalis:
* 2017年5月27日 上午9:57:11
*/

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DateUtil {

	private static Map<String,SimpleDateFormat> smfMap = new HashMap<>();
	
	static {
		smfMap.put("yyyy-MM", new SimpleDateFormat("yyyy-MM"));
		smfMap.put("yyyy-MM-dd", new SimpleDateFormat("yyyy-MM-dd"));
		smfMap.put("yyyy-MM-dd hh:mm", new SimpleDateFormat("yyyy-MM-dd hh:mm"));
		smfMap.put("yyyy-MM-dd hh:mm:ss", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
		smfMap.put("yyyy-MM-dd HH:mm:ss", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		smfMap.put("yyyy.MM", new SimpleDateFormat("yyyy.MM"));
		smfMap.put("yyyy", new SimpleDateFormat("yyyy"));
		smfMap.put("yyyy.MM.dd", new SimpleDateFormat("yyyy.MM.dd"));
	}
	
	public static String format (String format , Date date) {
		SimpleDateFormat smf = getSmf(format);
		return smf.format(date);
	}
	
	public static String format (String format , Long timemills) {
		SimpleDateFormat smf = getSmf(format);
		return smf.format(new Date(timemills));
	}
	
	public static SimpleDateFormat getSmf (String format) {
		SimpleDateFormat smf = smfMap.get(format);
		if(smf == null) {
			smf = new SimpleDateFormat(format);
			smfMap.put(format, smf);
		}
		return smf;
	}
	
	public synchronized static Date parse (String format, String date) {
		SimpleDateFormat smf = smfMap.get(format);
		try {
			return smf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Long getBeiJingSeconds() {
		Calendar result = new GregorianCalendar(TimeZone.getTimeZone("GMT+08:00"));
		return result.getTimeInMillis()/1000;
	}
	
}
