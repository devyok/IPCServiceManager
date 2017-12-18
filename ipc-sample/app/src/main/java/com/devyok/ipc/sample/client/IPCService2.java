package com.devyok.ipc.sample.client;

import android.os.IBinder;
import android.os.RemoteException;

import com.devyok.ipc.IPCConfig;
import com.devyok.ipc.IPCService;
import com.devyok.ipc.sample.IRemoteService2;
import com.devyok.ipc.utils.LogControler;

@IPCConfig(isExported = false,processName = "impl2ProcessName",serviceName = "impl2ServiceName")
public class IPCService2 extends IPCService {

    IRemoteService2 service = new IRemoteService2.Stub() {

        @Override
        public void run(String p1) throws RemoteException {
            LogControler.info("IPCService2", "[svcmgr ipcservice2] run = " + p1);
        }
    };

    @Override
    public IBinder getService(String selection) {
        LogControler.info("IPCService2", "[svcmgr ipcservice2] getService enter");
        return service.asBinder();
    }

    @Override
    public void onServiceCreate() {
        LogControler.info("IPCService2", "[svcmgr ipcservice2] onCreate enter");
    }
}
