package com.devyok.ipc.sample.client;

import android.os.IBinder;
import android.os.RemoteException;

import com.devyok.ipc.IPCConfig;
import com.devyok.ipc.IPCService;
import com.devyok.ipc.sample.IRemoteService1;
import com.devyok.ipc.utils.LogControler;

/**
 * Created by wei.deng on 2017/12/13.
 */
@IPCConfig(isExported = false,processName = "impl1ProcessName",serviceName = "impl1ServiceName")
public class IPCService1 extends IPCService {

    IRemoteService1 service = new IRemoteService1.Stub() {

        @Override
        public void run(String p1) throws RemoteException {
            LogControler.info("IPCService1", "[svcmgr ipcservice1] run = " + p1);
        }
    };

    @Override
    public IBinder getService(String selection) {
        LogControler.info("IPCService1", "[svcmgr ipcservice1] getService enter");
        return service.asBinder();
    }

    @Override
    public void onServiceCreate() {
        LogControler.info("IPCService1", "[svcmgr ipcservice1] onCreate enter");
    }
}
