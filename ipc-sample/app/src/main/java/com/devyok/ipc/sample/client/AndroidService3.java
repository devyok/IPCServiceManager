package com.devyok.ipc.sample.client;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.devyok.ipc.ServiceCallback;
import com.devyok.ipc.ServiceManager;
import com.devyok.ipc.exception.IPCException;
import com.devyok.ipc.exception.IPCServiceNotFoundException;
import com.devyok.ipc.exception.IPCTimeoutException;
import com.devyok.ipc.sample.IRemoteService1;
import com.devyok.ipc.sample.IRemoteService2;
import com.devyok.ipc.utils.LogControler;

public class AndroidService3 extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		
		LogControler.info("AndroidService3", "[svcmgr service3] onBind enter");
		
		return new Binder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogControler.info("AndroidService3", "[svcmgr service3] onCreate enter");
		
		try {
			ServiceManager.asyncGetService("service1", new ServiceCallback() {
				
				@Override
				public void callback(IBinder service) throws RemoteException {
					
					IRemoteService1 remoteService = IRemoteService1.Stub.asInterface(service);
					remoteService.run("invoke service1");
					
				}
			});

			ServiceManager.asyncGetService("service2", new ServiceCallback() {

				@Override
				public void callback(IBinder service) throws RemoteException {

					IRemoteService2 remoteService = IRemoteService2.Stub.asInterface(service);
					remoteService.run("invoke service2");

				}
			});

		} catch (IPCServiceNotFoundException e) {
			e.printStackTrace();
		} catch (IPCTimeoutException e) {
			e.printStackTrace();
		} catch (IPCException e) {
			e.printStackTrace();
		}

		
	}
	
	

}
