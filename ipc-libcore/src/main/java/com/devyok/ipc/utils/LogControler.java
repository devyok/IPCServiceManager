package com.devyok.ipc.utils;

import android.util.Log;

/**
 * @author DengWei
 */
public final class LogControler {

	private static boolean DEBUG = false;
	
	public static void enableDebug(){
		DEBUG = true;
	}
	
	public static void disableDebug(){
		DEBUG = false;
	}
	
	
	public static void info(String tag,String msg){
		Log.i(tag, msg);
	}
	
	public static void debug(String tag,String msg){
		if(DEBUG) {
			Log.i(tag, msg);
		}
	}
	
	public static void error(String tag,String msg){
		Log.e(tag, msg);
	}
	
}
