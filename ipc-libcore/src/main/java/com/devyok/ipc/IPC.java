package com.devyok.ipc;

import android.content.Context;
import android.net.Uri;
import android.os.Process;

import com.devyok.ipc.utils.Utils;
/**
 * @author DengWei
 */
class IPC {
	
	static Uri SERVICE_MANAGER_URI = Uri.parse("content://ipc_servicemanager/client");
	
	static final String PROCESS_NAME = ":svcmgr";

	static String sCurrentProcess;
	static int sCurrentPid;
	static String sPackageName;
	
	static boolean sIsUIProcess;
	static boolean sIsSvcmgrProcess;
	
	static void init(Context context){
		sCurrentProcess = Utils.getCurrentProcessName();
        sCurrentPid = Process.myPid();
        sPackageName = context.getApplicationInfo().packageName;

        sIsUIProcess = sCurrentProcess.equals(sPackageName);
        sIsSvcmgrProcess = sCurrentProcess.contains(PROCESS_NAME);
        
	}
	
	public static String getCurrentProcessName(){
		return sCurrentProcess;
	}
	
	public static int getCurrentPid(){
		return sCurrentPid;
	}
	
	public static boolean isUIProcess(){
		return sIsUIProcess;
	}
	
	public static boolean isSvcmgrProcess(){
		return sIsSvcmgrProcess;
	}
	
	
}
