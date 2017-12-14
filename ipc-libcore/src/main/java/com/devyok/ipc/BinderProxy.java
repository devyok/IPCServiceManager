package com.devyok.ipc;

import java.io.FileDescriptor;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.devyok.ipc.exception.IPCException;

public class BinderProxy implements IBinder , IBinder.DeathRecipient{

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
		
		if(target == null){
			try {
				this.target = ServiceManager.getServiceInternal(this.serviceName);
			} catch (IPCException e) {
				e.printStackTrace();
			}
		}
		
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
	}

	@Override
	public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
		return false;
	}

	@Override
	public void binderDied() {
		target = null;
	}

}
