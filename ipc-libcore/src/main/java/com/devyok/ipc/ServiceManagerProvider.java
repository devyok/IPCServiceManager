package com.devyok.ipc;

import android.os.IBinder;

import com.devyok.ipc.utils.LogControler;

import java.util.concurrent.ConcurrentHashMap;
/**
 * @author DengWei
 */
public class ServiceManagerProvider extends IPCService{

	private static final String LOG_TAG = ServiceManagerProvider.class.getSimpleName();

	public static ConcurrentHashMap<String,IPCServiceInfo> ipcServiceInfos = new ConcurrentHashMap<String, IPCServiceInfo>();

	public ServiceManagerProvider(){
		LogControler.debug(LOG_TAG, "[svcmgr server] SerivceManagerProvider create");
	}

	@Override
	public IBinder getService(String selection) {
		
		if("server".equals(selection)) {
			return ServiceManagerServer.INSTANCE.asBinder();
		}
		
		return null;
	}

	@Override
	public void onServiceCreate() {
		
	}



}
