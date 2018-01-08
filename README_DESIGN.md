# IPCServiceManager

Android进程间通信框架

## 设计 ##
设计相关大图均在代码根目录下， 更多请参考[源码分析](http://blog.csdn.net/degwei);


![](https://raw.githubusercontent.com/devyok/IPCServiceManager/master/ServiceManager.png)


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

[如何集成使用](https://github.com/devyok/IPCServiceManager/blob/master/README.md)

## License ##
ServiceManager is released under the [Apache 2.0 license](https://github.com/devyok/ServiceManager/blob/master/LICENSE).

Copyright (C) 2017 DengWei.