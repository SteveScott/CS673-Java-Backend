package main.java;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WhatTime {
	
	public WhatTime(){}
	
	public static String now(){
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		return time;
	}
}
