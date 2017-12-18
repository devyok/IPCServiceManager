package com.devyok.ipc;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.devyok.ipc.exception.IPCException;
import com.devyok.ipc.utils.LogControler;

import java.io.FileDescriptor;

/**
 * @author DengWei
 */
class BinderProxy implements IBinder , IBinder.DeathRecipient{

	static final String LOG_TAG = "BinderProxy";

	private IBinder target;
	private String serviceName;
	
	public BinderProxy(String serviceName,IBinder target){
		this.serviceName = serviceName;
		this.target = target;
	}
	
	public static IBinder proxy(String serviceName,IBinder target) {
		BinderProxy binderProxy = new BinderProxy(serviceName,target);
		try {
			target.linkToDeath(binderProxy, 0);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return binderProxy;
	}
	
	private IBinder getBinder() throws RemoteException {

		LogControler.info(LOG_TAG, "getBinder transact = " + target + " , target.isBinderAlive() = " + target.isBinderAlive() + " , target.pingBinder() = " + target.pingBinder());

		if((target == null) || (target!=null && (!target.isBinderAlive() || !target.pingBinder()))){
			try {

				LogControler.info(LOG_TAG, "start get service from svcmgr");

				this.target = ServiceManager.getServiceInternal(this.serviceName);

				LogControler.info(LOG_TAG, "getServiceInternal target = " + target);

			} catch (IPCException e) {
				e.printStackTrace();
			}
		}

		LogControler.info(LOG_TAG, "getBinder final target = " + target);
		
		if(target==null) {
			throw new RemoteException();
		}
		
		return this.target;
	}
	
	@Override
	public String getInterfaceDescriptor() throws RemoteException {
		return getBinder().getInterfaceDescriptor();
	}

	@Override
	public boolean pingBinder() {
		try {
			return getBinder().pingBinder();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isBinderAlive() {
		try {
			return getBinder().isBinderAlive();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public IInterface queryLocalInterface(String descriptor) {
		try {
			return getBinder().queryLocalInterface(descriptor);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void dump(FileDescriptor fd, String[] args) throws RemoteException {
		getBinder().dump(fd, args);
	}

	@Override
	public void dumpAsync(FileDescriptor fd, String[] args)
			throws RemoteException {
		getBinder().dumpAsync(fd, args);
	}

	@Override
	public boolean transact(int code, Parcel data, Parcel reply, int flags)
			throws RemoteException {

		return getBinder().transact(code, data, reply, flags);
	}

	@Override
	public void linkToDeath(DeathRecipient recipient, int flags)
			throws RemoteException {
		getBinder().linkToDeath(recipient,flags);
	}

	@Override
	public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
		return false;
	}

	@Override
	public void binderDied() {
		LogControler.info(LOG_TAG, "binder died {"+serviceName+"}");
		target = null;
	}

}
