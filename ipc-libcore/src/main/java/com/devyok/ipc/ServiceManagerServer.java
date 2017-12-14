package com.devyok.ipc;

import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.devyok.ipc.utils.LogControler;

import java.util.concurrent.ConcurrentHashMap;

class ServiceManagerServer extends IServiceManager.Stub {

	static IServiceManager INSTANCE = new ServiceManagerServer();
	
	final ConcurrentHashMap<String, IBinder> clientServiceContainer = new ConcurrentHashMap<String, IBinder>();
	
	final ConcurrentHashMap<String, IServiceCallback> asyncClientServiceContainer = new ConcurrentHashMap<String, IServiceCallback>();

	@Override
	public IBinder getService(String serviceName) throws RemoteException {
		LogControler.info("ServiceManagerServer", "[svcmgr server] getService name = " + serviceName + " , callingpid = " + Binder.getCallingPid());
		
		IBinder service = clientServiceContainer.get(serviceName);

		LogControler.info("ServiceManagerServer", "[svcmgr server] getService from container result = " + service);

		if(service == null){
			IBinder queryResult = BinderQuerier.query(serviceName);
			addService(serviceName,queryResult);
			return queryResult;
		}

		return service;
	}

	@Override
	public void addService(String serviceName, IBinder service)
			throws RemoteException {

		LogControler.info("ServiceManagerServer", "[svcmgr server] addService name = " + serviceName + " , service = " + service+ " , callingpid = " + Binder.getCallingPid());
		
		clientServiceContainer.put(serviceName, service);

		//查看是否有listen这个服务的
		IServiceCallback serviceCallback = asyncClientServiceContainer.get(serviceName);
		
		LogControler.info("ServiceManagerServer", "[svcmgr server] addService serviceCallback not found");
		
		if(serviceCallback!=null){
			try {
				LogControler.info("ServiceManagerServer", "[svcmgr server] addService onReady");
				serviceCallback.callback(service);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}


	@Override
	public void asyncGetService(String serviceName, IServiceCallback callback)
			throws RemoteException {
		LogControler.info("ServiceManagerServer", "[svcmgr server] asyncGetService name = " + serviceName + " , callingpid = " + Binder.getCallingPid());
		IBinder service = clientServiceContainer.get(serviceName);
		
		if(service == null) {
			service = BinderQuerier.query(serviceName);
		}

		if(service == null){
			asyncClientServiceContainer.put(serviceName, callback);
		} else {
			callback.callback(service);
		}
		
	}



	
}
