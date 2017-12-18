package com.devyok.ipc;

import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.devyok.ipc.utils.LogControler;
import com.devyok.ipc.utils.Utils;

import java.util.concurrent.ConcurrentHashMap;
/**
 * @author DengWei
 */
class ServiceManagerServer extends IServiceManager.Stub {

	static ServiceManagerServer INSTANCE = new ServiceManagerServer();
	
	private final ConcurrentHashMap<String, IBinder> clientServiceContainer = new ConcurrentHashMap<String, IBinder>();

	private final ConcurrentHashMap<String, IServiceCallbackProxy> asyncClientServiceContainer = new ConcurrentHashMap<String, IServiceCallbackProxy>();

	@Override
	public IBinder getService(String serviceName) throws RemoteException {
		LogControler.info("ServiceManagerServer", "[svcmgr server] getService name = " + serviceName + " , callingpid = " + Binder.getCallingPid());
		
		IBinder service = clientServiceContainer.get(serviceName);

		LogControler.info("ServiceManagerServer", "[svcmgr server] getService from container result = " + service);

		if(service == null || !Utils.isAlive(service)){
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

		service.linkToDeath(new BinderServiceDeathRecipient(serviceName),0);
		clientServiceContainer.put(serviceName, service);

		//查看是否有listen这个服务的
		IServiceCallbackProxy serviceCallback = asyncClientServiceContainer.get(serviceName);

		if(serviceCallback!=null){
			try {
				LogControler.info("ServiceManagerServer", "[svcmgr server] addService onReady");
				serviceCallback.callback(service);
			} catch(Exception e){
				e.printStackTrace();
			}
		} else {
			LogControler.info("ServiceManagerServer", "[svcmgr server] addService serviceCallback not found");
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
			asyncClientServiceContainer.put(serviceName, IServiceCallbackProxy.proxy(serviceName,callback));
		} else {
			callback.callback(service);
		}
		
	}

	static class BinderServiceDeathRecipient implements IBinder.DeathRecipient{
		private String serviceName;
		private BinderServiceDeathRecipient(String serviceName){
			this.serviceName = serviceName;
		}

		@Override
		public void binderDied() {
			LogControler.info("BinderServiceDeathRecipient", "[svcmgr server] ("+serviceName+") process died");
			ServiceManagerServer.INSTANCE.clientServiceContainer.remove(serviceName);
		}

	}

	static class IServiceCallbackProxy implements IBinder.DeathRecipient{
		private IServiceCallback callback;
		private String serviceName;
		private IServiceCallbackProxy(String serviceName,IServiceCallback callback){
			this.serviceName = serviceName;
			this.callback = callback;
		}

		static IServiceCallbackProxy proxy(String serviceName,IServiceCallback callback) throws RemoteException{
			IServiceCallbackProxy proxy = new IServiceCallbackProxy(serviceName,callback);
			callback.asBinder().linkToDeath(proxy,0);
			return proxy;
		}

		@Override
		public void binderDied() {
			LogControler.info("IServiceCallbackProxy", "[svcmgr server] async getservice("+serviceName+") process died");
			ServiceManagerServer.INSTANCE.asyncClientServiceContainer.remove(serviceName);
			callback = null;
		}

		public void callback(IBinder binder) throws RemoteException{
			if(callback!=null){
				callback.callback(binder);
				ServiceManagerServer.INSTANCE.asyncClientServiceContainer.remove(serviceName);
			} else {
				LogControler.info("IServiceCallbackProxy", "[svcmgr server] callback is null, It may be due to asyncgetservice("+serviceName+") process died");
			}
		}
	}


}
