[![license](http://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat)](https://github.com/devyok/ServiceManager/blob/master/LICENSE)
[![Release Version](https://img.shields.io/badge/release-0.0.1-brightgreen.svg)](https://jcenter.bintray.com/com/devyok/web/hybridmessenger-core/0.0.1/)


# IPCServiceManager

Android进程间通信框架

背景： 为了应对移动应用内存限制的问题，移动应用通常进行多进程化(根据职责驱动原则和模式分解，分割业务到不同的进程中提高应用稳定性)，而多进程间通信的实现方式有多种方法，比如：aidl或定义自己binder通信接口进行通信(参考：[DroidIPC](https://github.com/devyok/DroidIPC) )。 ServiceManager是基于ContentProvider获取aidl接口的方式。

[看看框架设计](https://github.com/devyok/IPCServiceManager/blob/master/README_DESIGN.md)

![](https://raw.githubusercontent.com/devyok/IPCServiceManager/master/ServiceManager.png)

## 优势 ##

- 大大的降低了进程间的依赖，增强了灵活性，扩展性与可读性；
- 通过Annotation注解的方式声明远程服务，提升了开发效率；
- 为了简化开发人员的配置和降低错误，ServiceManager提供gradle插件来帮助开发人员在编译阶段完成配置；


## 技术实现 ##
IPCServiceManager(简称：svcmgr)的实现并不复杂，但凡使用svcmgr根据自身需求，可能需要继承IPCService(不是强制)，框架要求实现者提供IBinder接口，同时并通过IPCConfig(Annotation)来声明进程信息。svcmgr通过[gradle plugin](https://github.com/devyok/IPCServiceManager/blob/master/plugin_design_flow.png)拦截manifest process task，解析Annotation，每一个通过IPCConfig配置的服务都会在Mainfest文件中对应一个provider节点，根据声明生成新的AndroidMainfest.xml文件，一起打包到APK中。框架在init阶段会获取Manifest中的provider信息生成对应的URI即可实现通信。 具体实现请见源码。



## 如何使用 ##
[直接看实例代码](https://github.com/devyok/ServiceManager/tree/master/ipc-sample)
### 第一步 ###
在gradle中引入ServiceManager插件

	buildscript {
	    dependencies {
	        classpath 'com.devyok.ipc:ipc-gradle-plugin:0.0.1'
	    }
	}

### 第二步 ###
在gradle中引入ipc-libcore
	
	
	dependencies {
	    compile 'com.devyok.ipc:ipc-libcore:0.0.2'
	}
	
### 第三步 ###
在Application#onCreate中初始化ServiceManager

	ServiceManager.init(getApplicationContext());

在初始化之前你可以打开ServiceManager的调试开发

	if(BuildConfig.DEBUG){
		LogControler.enableDebug();
	}
		
### 第四步 ###
实现远程服务

1. 首先你需要先继承IPCService,框架要求必须实现下面两个方法（getService与onServiceCreate）,getService返回IBinder接口，
这个接口的实现，可以通过aidl生成也可以extends Binder来实现。onServiceCreate方法主要提供一个初始化服务的机会。
		
		IRemoteService1.aidl
		interface IRemoteService1 {
			void run(String p1);
		}	
		
		IPCService1.java
		public class IPCService1 extends IPCService {

		    @Override
		    public IBinder getService(String selection) {
		        return null;
		    }
		
		    @Override
		    public void onServiceCreate() {
		        
		    }
		}

2.通过IPCConfig来声明此服务器的进程信息
	
	@IPCConfig(isExported = false,processName = "sample.p1",serviceName = "sample.s1") 

### 第五步 ###
获取并调用服务

	IBinder service = ServiceManager.getService("sample.s1");
    IRemoteService1 client1 = IRemoteService1.Stub.asInterface(service);
	client1.run("params");


## 其他用法 ##

不需要通过继承IPCService，可直接通过ServiceManager#addService添加一个服务。 使用与上面方法一样。 通过ServiceManager.getService("xx");获取并使用。

	ServiceManager.addService("xx", new IRemoteService1.Stub() {
				
		@Override
		public void run(String p1) throws RemoteException {
			LogControler.info("AndroidService1", "[svcmgr service1] run = " + p1);
		}
	});

## 设计 ##
设计相关大图均在代码根目录下， 更多请参考[源码分析](http://blog.csdn.net/degwei)

**各组件依赖关系，见下图：**

![](https://raw.githubusercontent.com/devyok/ServiceManager/master/lib_design_component.png)

**包依赖，见下图：**

![](https://raw.githubusercontent.com/devyok/ServiceManager/master/lib_design_package.png)

**lib-core类图，见下图：**

![](https://raw.githubusercontent.com/devyok/ServiceManager/master/lib_design_class_core.png)

**lib-exception类图，见下图：**

![](https://raw.githubusercontent.com/devyok/IPCServiceManager/master/lib_design_class_exception.png)

**各模块调用时序，见下图：**

![](https://raw.githubusercontent.com/devyok/IPCServiceManager/master/lib_design_seq_module_between_communication.png)

**初始化(ServiceManager#init)，见下图：**

![](https://raw.githubusercontent.com/devyok/IPCServiceManager/master/lib_design_seq_servicemanager_init.png)

**获取服务(ServiceManager#getService)，见下图：**

![](https://raw.githubusercontent.com/devyok/ServiceManager/master/lib_design_seq_servicemanager_getservice.png)

**添加服务(ServiceManager#addService)，见下图：**

![](https://raw.githubusercontent.com/devyok/IPCServiceManager/master/lib_design_seq_servicemanager_addservice.png)

## 插件执行流程 ##
![](https://raw.githubusercontent.com/devyok/IPCServiceManager/master/plugin_design_flow.png)

## License ##
ServiceManager is released under the [Apache 2.0 license](https://github.com/devyok/ServiceManager/blob/master/LICENSE).

Copyright (C) 2017 DengWei.
