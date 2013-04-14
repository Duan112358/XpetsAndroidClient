package com.emacs.xpets.utils;

import java.util.Date;

import com.generpoint.xpets.BuildConfig;

import android.util.Log;

public class MLog{
	public static void i(String msg){
		if(BuildConfig.DEBUG){
			Log.i(Constants.TAG, msg);
		}
	}
	
	public static void error(String error){
		Date date = new Date();
		Log.e(Constants.ERROR, "Error occured : "+ date.toString());
		Log.e(Constants.ERROR, error);
	}
}
