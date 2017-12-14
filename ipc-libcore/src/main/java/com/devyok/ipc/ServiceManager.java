package com.devyok.ipc;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import com.devyok.ipc.exception.IPCException;
import com.devyok.ipc.exception.IPCRuntimeException;
import com.devyok.ipc.exception.IPCServiceNotFoundException;
import com.devyok.ipc.exception.IPCTimeoutException;
import com.devyok.ipc.utils.LogControler;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author wei.deng
 *
 */
public final class ServiceManager{

	private static IServiceManager remoteProxy;
	
	static final ConcurrentHashMap<String, ServiceCallback> asyncClientServiceCallbacks = new ConcurrentHashMap<String, ServiceCallback>();
	
	public static void init(Context context){
		
		if(context == null){
			throw new IPCRuntimeException("context param must be not null");
		}
		
		IPC.init(context);
		
		if(!IPC.isSvcmgrProcess()){
			
			LogControler.debug("ServiceManager", "[svcmgr proxy] create proxy object, current process name = " + IPC.getCurrentProcessName());
			
			remoteProxy = RemoteServiceManagerProxy.create(context);
		} else {

			BinderQuerier.init(context);

		}
		
	}
	
	static void check(){
		if(remoteProxy == null){
			throw new IPCRuntimeException("please init ServiceManager");
		}
	}
	
	public static IBinder getService(String serviceName) throws IPCServiceNotFoundException,IPCTimeoutException,IPCException {
		
		LogControler.info("ServiceManager", "[svcmgr proxy] getService("+serviceName+") , cpn = " + IPC.getCurrentProcessName() + " , cpid = " + IPC.getCurrentPid());
		
		IBinder binder = getServiceInternal(serviceName);
		return new BinderProxy(serviceName,binder);
	}
	
	public static void asyncGetService(String serviceName,ServiceCallback serviceCallback) throws IPCServiceNotFoundException,IPCTimeoutException,IPCException {

		LogControler.info("ServiceManager", "[svcmgr proxy] asyncGetService("+serviceName+") , cpn = " + IPC.getCurrentProcessName() + " , cpid = " + IPC.getCurrentPid());
		check();
		
		try {
			remoteProxy.asyncGetService(serviceName, serviceCallback);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPCException("asyncget ipc service("+serviceName+") error" , e);
		}
		
	}
	
	public static void addService(String serviceName, IBinder service)throws IPCException {

		LogControler.info("ServiceManager", "[svcmgr proxy] addService("+serviceName+") , cpn = " + IPC.getCurrentProcessName() + " , cpid = " + IPC.getCurrentPid());

		check();
		
		try {
			remoteProxy.addService(serviceName, service);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPCException("add ipc service("+serviceName+") error" , e);
		}
		
	}

	static IBinder getServiceInternal(String serviceName) throws IPCServiceNotFoundException,IPCTimeoutException,IPCException {

		check();

		try {

			IBinder binder = remoteProxy.getService(serviceName);

			if(binder == null){
				throw new IPCServiceNotFoundException("ipc service("+serviceName+") not found");
			}

			return binder;

		} catch (Exception e) {
			e.printStackTrace();
			throw new IPCException("query ipc service("+serviceName+") error" , e);
		}
	}
	
	static final class RemoteServiceManagerProxy implements IServiceManager{
		
		private IServiceManager serviceManager;
		
		public RemoteServiceManagerProxy(IBinder binder) {
			serviceManager = IServiceManager.Stub.asInterface(binder);
		}

		public static IServiceManager create(Context context) {
			return new RemoteServiceManagerProxy(BinderQuerier.query(context,IPC.SERVICE_MANAGER_URI,"server"));
		}

		@Override
		public IBinder asBinder() {
			return serviceManager.asBinder();
		}

		@Override
		public IBinder getService(String serviceName) throws RemoteException {
			return serviceManager.getService(serviceName);
		}

		@Override
		public void addService(String serviceName, IBinder service) throws RemoteException {
			serviceManager.addService(serviceName, service);
		}

		@Override
		public void asyncGetService(String serviceName,
				IServiceCallback callback) throws RemoteException {
			serviceManager.asyncGetService(serviceName, callback);
		}
		
		
	}
	
}
