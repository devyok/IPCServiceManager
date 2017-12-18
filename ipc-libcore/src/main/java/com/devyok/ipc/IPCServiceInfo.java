package com.devyok.ipc;

import android.content.pm.ProviderInfo;
import android.net.Uri;

/**
 * @author DengWei
 */
class IPCServiceInfo {

    /**
     *  String authority = providerInfo.authority;
        boolean isExported = providerInfo.exported;
        String name = providerInfo.name;
        String processName = providerInfo.processName;
     */
    public ProviderInfo providerInfo;

    public String toString(){
        return "authority = " + providerInfo.authority + " , class name = " + providerInfo.name + ", processName = " + providerInfo.processName;
    }

    public Uri getUri(){
        return Uri.parse("content://"+providerInfo.authority+"/client");
    }

}
