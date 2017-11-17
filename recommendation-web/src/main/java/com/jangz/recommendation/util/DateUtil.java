package com.jangz.recommendation.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String getCurrentTimeMillis() {
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(date);
	}
	
	public static int getSecond(int addSecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, addSecond);
		
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int secondOfHour = calendar.get(Calendar.SECOND);
        int second = hour * 3600 + minute * 60 + secondOfHour;
        return second;
	}
	
	public static String getDay(int addSecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, addSecond);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
	}
	
	public static void main(String arg[]) {
		int addSecond = -86400;
		System.out.println(DateUtil.getDay(addSecond));
		System.out.println(DateUtil.getSecond(addSecond));
	}
}
