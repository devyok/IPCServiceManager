package com.devyok.ipc;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
/**
 * @author DengWei
 */
public abstract class ServiceCallback extends IServiceCallback.Stub{

	@Override
	public IBinder asBinder() {
		return super.asBinder();
	}

	@Override
	public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
			throws RemoteException {
		return super.onTransact(code, data, reply, flags);
	}
	
}
