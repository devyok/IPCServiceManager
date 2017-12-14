package com.devyok.ipc;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import com.devyok.ipc.exception.IPCRuntimeException;
import com.devyok.ipc.utils.LogControler;

import java.util.concurrent.ConcurrentHashMap;

final class BinderQuerier {

	static Context sContext;

	static ConcurrentHashMap<String,IPCServiceInfo> ipcServiceInfos = new ConcurrentHashMap<String, IPCServiceInfo>();
	
	static final IBinder query(Context context,Uri uri,String name){
		Cursor c = context.getContentResolver().query(uri, null, name, null, null);
		IBinder binder = query(c);
		return binder;
	}

	static final IBinder query(Cursor cursor) {
		Bundle bundle = cursor.getExtras();
		bundle.setClassLoader(BinderParcel.class.getClassLoader());
		BinderParcel parcelBinder = bundle.getParcelable(BinderCusrsor.BINDER_CURSOR);
		return parcelBinder.getIbinder();
	}


	static IBinder query(String serviceName){
		IPCServiceInfo ipcServiceInfo = ipcServiceInfos.get(serviceName);

		LogControler.info("ServiceManagerServer", "[svcmgr server] query = " + ipcServiceInfo);

		if(ipcServiceInfo == null){
			return null;
		}

		Uri serviceUri = ipcServiceInfo.getUri();

		LogControler.info("ServiceManagerServer", "[svcmgr server] query serviceUri = " + serviceUri);

		IBinder queryResult = query(sContext,serviceUri,serviceName);

		LogControler.info("ServiceManagerServer", "[svcmgr server] query result = " + queryResult);

		return queryResult;
	}


	static void check(){
		if(sContext==null){
			throw new IPCRuntimeException("please init ServiceManagerProvider");
		}
	}

	static void init(Context context){

		sContext = context;

		PackageManager pm = context.getPackageManager();
		try {

			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),PackageManager.GET_PROVIDERS | PackageManager.GET_META_DATA);

			ProviderInfo providerInfos[] = packageInfo.providers;

			for (ProviderInfo item : providerInfos) {

				Bundle metaData = item.metaData;

				if(metaData!=null){

					String value = metaData.getString("ipc");
					if(value!=null){
						IPCServiceInfo ipcServiceInfo = new IPCServiceInfo();
						ipcServiceInfo.providerInfo = item;

						ipcServiceInfos.put(ipcServiceInfo.providerInfo.authority,ipcServiceInfo);

						LogControler.info("ServiceManagerServer", "[svcmgr server] ipcServiceInfo = " + ipcServiceInfo);
					}

				}
			}

		} catch(Exception e){
			e.printStackTrace();
		}

	}
	
}
