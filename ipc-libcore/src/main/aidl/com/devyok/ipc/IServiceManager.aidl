package com.devyok.ipc;

import com.devyok.ipc.IServiceCallback;

interface IServiceManager {

    IBinder getService(String serviceName);
    
    void asyncGetService(String serviceName,in IServiceCallback callback);

    void addService(String serviceName, IBinder service);

}