package com.devyok.ipc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IPCConfig {

    String serviceName();
    String processName();
    boolean isExported() default false;

    public interface ClassInfo {
        public static String CLASS_PACKAGE = "com.devyok.ipc.autojava";
        public static String CLASS_NAME = "IPCHelper";
        /**
         * 值为json
         */
        public static String CONFIG_LIST = "configList";
    }

}
