package com.apache.flink.jointestcase;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	
	
	public static Date convertTimeStamp(String timestamp) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		Date date;
		try {
//			System.out.println("timestamp>>>>>>>>>>>"+timestamp);
			date = formatter.parse(timestamp);
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public static String convertDateToString(LocalDateTime timestamp) {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String date = formatter.format(timestamp);
		return date;
	}
	
	public static String convertLongToTimeStamp(long convertLongToTimeStamp) {
		if(convertLongToTimeStamp < 0)
			return "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

		LocalDateTime time =  LocalDateTime.ofInstant(Instant.ofEpochMilli(convertLongToTimeStamp), TimeZone
		        .getDefault().toZoneId());
//		ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(convertLongToTimeStamp), 
//                ZoneId.systemDefault());
//		System.out.println("printing date "+formatter.format(time)+ " long "+convertLongToTimeStamp);
		return formatter.format(time);
		
	}
	
	public static void main(String[] args) {
		String date = "2022-11-01T11:18:45.227+0000";
		Date d = DateUtil.convertTimeStamp(date);
		System.out.println(d.getTime());
		
	}
}
