package com.jangz.recommendation.util;

public class StringUtil {

	public static String toFirstUpperCase(String str) {
		if(str == null || str.length() < 1) {
			return "";
		}
		String start = str.substring(0,1).toUpperCase();
		String end = str.substring(1, str.length());
		return start + end;
	}

	public static String limitTitle(String title,int count) {
		if(title != null && title.length() > count) {
			return title.substring(0, count) + "...";
		}
		return title;
	}

	public static String clearTitleAll(String title) {
		String result = clearTitle(title, "-");
		result = clearTitle(result, "_");
		return result;
	}

	public static String clearTitle(String title, String symbol) {
		if(title == null) {
			return "";
		}
		int index = title.lastIndexOf(symbol);
		return index < 5 ? title : clearTitle(title.substring(0, index), symbol);
	}
	
	public static void main(String [] args) {
		String title = "习近平访问欧盟";
		System.out.println(StringUtil.limitTitle(title, 5));
	}
}
