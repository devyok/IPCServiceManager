package com.devyok.ipc.sample.client;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.devyok.ipc.ServiceManager;
import com.devyok.ipc.exception.IPCException;
import com.devyok.ipc.sample.IRemoteService2;
import com.devyok.ipc.utils.LogControler;

public class AndroidService2 extends Service{
	@Override
	public IBinder onBind(Intent intent) {
		
		LogControler.info("AndroidService2", "[svcmgr service2] onBind enter");
		
		return new Binder();
	}

	
	
	@Override
	public void onCreate() {
		super.onCreate();
		LogControler.info("AndroidService2", "[svcmgr service2] onCreate enter");
		
		try {
			ServiceManager.addService("service2", new IRemoteService2.Stub() {
				
				@Override
				public void run(String p1) throws RemoteException {
					LogControler.info("AndroidService2", "[svcmgr service2] run  = " + p1);
				}
			});
		} catch (IPCException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
