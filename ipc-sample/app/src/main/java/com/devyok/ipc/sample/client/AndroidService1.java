package com.devyok.ipc.sample.client;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.devyok.ipc.ServiceManager;
import com.devyok.ipc.exception.IPCException;
import com.devyok.ipc.sample.IRemoteService1;
import com.devyok.ipc.utils.LogControler;

public class AndroidService1 extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		LogControler.info("AndroidService1", "[svcmgr service1] onBind enter");
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogControler.info("AndroidService1", "[svcmgr service1] onCreate enter");
		
		try {
			ServiceManager.addService("service1", new IRemoteService1.Stub() {
				
				@Override
				public void run(String p1) throws RemoteException {
					LogControler.info("AndroidService1", "[svcmgr service1] run = " + p1);
				}
			});
		} catch (IPCException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
