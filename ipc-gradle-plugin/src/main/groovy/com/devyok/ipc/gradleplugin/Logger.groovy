package com.devyok.ipc.gradleplugin

public class Logger  {

    def static DEBUG = false

    def static info(def msg){
        println msg
    }

    def static debug(def msg){
        if(DEBUG){
            println msg
        }
    }

}