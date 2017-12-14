package com.devyok.ipc.gradleplugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

public class IPCTask extends DefaultTask {

    @TaskAction
    void run(){

        println "run enter";
        
    }

}