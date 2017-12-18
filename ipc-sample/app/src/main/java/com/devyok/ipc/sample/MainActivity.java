package com.devyok.ipc.sample;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.devyok.ipc.ServiceManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.findViewById(R.id.invoke_ipcservice1).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                try {

                    IBinder service = ServiceManager.getService("impl1ServiceName");

                    Log.e("MainActivity","impl1ServiceName = " + service);

                    IRemoteService1 client1 = IRemoteService1.Stub.asInterface(service);

                    Log.e("MainActivity","client1 = " + client1);

                    client1.run("xxxxxxxxxxxxxxxxxxxxxx");

                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        this.findViewById(R.id.invoke_ipcservice2).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                try {

                    IBinder service = ServiceManager.getService("impl2ServiceName");

                    Log.e("MainActivity","impl2ServiceName = " + service);

                    IRemoteService2 client2 = IRemoteService2.Stub.asInterface(service);

                    Log.e("MainActivity","client2 = " + client2);

                    client2.run("yyyyyyyyyyyyyyyyyyyyyyyyy");

                } catch(Exception e){
                    e.printStackTrace();
                }

            }
        });

        this.findViewById(R.id.invoke_androidservice1).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startClientProcess("com.AndroidService3");
                startClientProcess("com.AndroidService2");
                startClientProcess("com.AndroidService1");
            }
        });

    }

    private void startClientProcess(String action){
        Intent intent = new Intent(action);
        intent.setPackage(getPackageName());
        bindService(intent, new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

}
