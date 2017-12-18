package com.devyok.ipc.sample;

import android.app.Application;

import com.devyok.ipc.ServiceManager;
import com.devyok.ipc.utils.LogControler;

public class SampleApplication extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		LogControler.enableDebug();

		LogControler.info("SampleApplication", "SampleApplication create");

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			public void uncaughtException(Thread var1, Throwable var2){

				Throwable cause = var2.getCause();
				while (cause != null) {
					LogControler.info("SampleApplication", "exception = " + cause.getMessage());
					cause = cause.getCause();
				}

				System.exit(-1);
			}

		});
		ServiceManager.init(getApplicationContext());
	}

	
	
}
